package application;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.Vector;

import data.ClientInfo;
import data.Controllers;
import data.Schedule;
import data.TimeLine;
import data.TimeLineCell;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;

public class AddingChallengesController extends TimeLine implements Initializable {

	@FXML TextField titleTextField;
	@FXML TextField hourTextField;
	@FXML TextField minTextField;
	@FXML ComboBox<String> challengeTimeComboBox; // TODO ComboBox + Algorithm
	@FXML Button doneButton;
	@FXML Button addMoreButton;
	@FXML Button cancelButton;
	@FXML Label error_msg;
	@FXML Button AddEssentialsButton;
	@FXML Button ShowTimeLineButton;
	@FXML Button AddChallengesButton;
	@FXML Button MoveToGroupButton;
	
	////////// timeLine //////////
    private int currentDayOfWeek; // 현재 보고있는 요일
    private boolean isChanged = false;
    Alert alert = new Alert(AlertType.CONFIRMATION, "완료를 누르지 않고 이동하면, 추가가 취소됩니다. 이동하시겠습니까?", ButtonType.NO, ButtonType.YES);
	
	Vector<Schedule> tempScheduleList = new Vector<>();
	//////////////////////////////
	
	////////// AddChallanges //////////
	private ObservableList<String> recommendList;
	private boolean isBlank = true;
	///////////////////////////////////
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		isChanged = false;
		
		Controllers.addingChallengesController = this;
		
		currentDayOfWeek = ClientInfo.today;
		AddEssentialsButton.setOnMouseClicked(event -> {
			if(isChanged) {
				alert.showAndWait();
				if(alert.getResult() == ButtonType.NO) {
					return;
				}
			}
			try {
				Parent second;
				second = FXMLLoader.load(getClass().getResource("templates/first.fxml"));
				second.getStylesheets().add(getClass().getResource("statics/application.css").toExternalForm());
				Scene sc = new Scene(second);
				Stage stage = (Stage)AddEssentialsButton.getScene().getWindow();
				stage.setScene(sc);
				stage.show();
			} catch(IOException e) {
				e.printStackTrace();
			}
		});
		ShowTimeLineButton.setOnMouseClicked(event -> {
			if(isChanged) {
				alert.showAndWait();
				if(alert.getResult() == ButtonType.NO) {
					return;
				}
			}
			try {
				Parent second;
				second = FXMLLoader.load(getClass().getResource("templates/TimeLine.fxml"));
				second.getStylesheets().add(getClass().getResource("statics/application.css").toExternalForm());
				Scene sc = new Scene(second);
				Stage stage = (Stage)ShowTimeLineButton.getScene().getWindow();
				stage.setScene(sc);
				stage.show();
			} catch(IOException e) {
				e.printStackTrace();
			}
		});
		AddChallengesButton.setOnMouseClicked(event -> {
			if(isChanged) {
				alert.showAndWait();
				if(alert.getResult() == ButtonType.NO) {
					return;
				}
			}
			try {
				Parent second;
				second = FXMLLoader.load(getClass().getResource("templates/AddingChallenges.fxml"));
				second.getStylesheets().add(getClass().getResource("statics/application.css").toExternalForm());
				Scene sc = new Scene(second);
				Stage stage = (Stage)AddChallengesButton.getScene().getWindow();
				stage.setScene(sc);
				stage.show();
			} catch(IOException e) {
				e.printStackTrace();
			}
		});
		MoveToGroupButton.setOnMouseClicked(event -> {
			if(isChanged) {
				alert.showAndWait();
				if(alert.getResult() == ButtonType.NO) {
					return;
				}
			}
			if(data.ClientInfo.groupId.split(";")[0].equals("null")) {
				try {
					Parent second;
					second = FXMLLoader.load(getClass().getResource("templates/groupGateway.fxml"));
					second.getStylesheets().add(getClass().getResource("statics/application.css").toExternalForm());
					Scene sc = new Scene(second);
					Stage stage = (Stage)MoveToGroupButton.getScene().getWindow();
					stage.setScene(sc);
					stage.show();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}else {
				try {
					Parent second;
					second = FXMLLoader.load(getClass().getResource("templates/groupMain.fxml"));
					second.getStylesheets().add(getClass().getResource("statics/application.css").toExternalForm());
					Scene sc = new Scene(second);
					Stage stage = (Stage)MoveToGroupButton.getScene().getWindow();
					stage.setScene(sc);
					stage.show();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		//////////timeLine ////////// TODO 필수일정 추가 하면 timeLine에 반영
		////////// 초기화 //////////
		timeLineListView.setFixedCellSize(cellHeight);
		timeLineList = FXCollections.observableArrayList();
		timeLineListView.setItems(timeLineList);
		///////////////////////////
		
		
		////////// 타임라인 일정 색상 표시 기능 //////////
		timeLineListView.setCellFactory(lv -> new TimeLineCell(this, selectedScheduleList));
		
		showSchedules(ClientInfo.dayOfWeekList[currentDayOfWeek]);
		///////////////////////////////////////////
		recommendList = FXCollections.observableArrayList();
		challengeTimeComboBox.setItems(recommendList);
		AddChallengesButton.setDisable(true);
		hourTextField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(!oldValue.equals(newValue) && newValue.length() > 0) {
					AddChallengesButton.setDisable(false);
					if(!isBlank && !minTextField.getText().equals("")) { // TODO
						recommendList.clear();
						recommendTime();
					}
					isBlank = false;
				}
				
				if(newValue.length() == 0) {
					AddChallengesButton.setDisable(true);
				}
			}
			
		});
		minTextField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				if(!oldValue.equals(newValue) && newValue.length() > 0) {
					AddChallengesButton.setDisable(false);
					if(!isBlank && !hourTextField.getText().equals("")) {
						recommendList.clear();
						recommendTime();
					}
				}
				
				if(newValue.length() == 0) {
					AddChallengesButton.setDisable(true);
				}
			}
			
		});
	}
	
	@FXML public void add() {
		if(titleTextField.getText().equals("") || hourTextField.getText().equals("") || minTextField.getText().equals("")) {
			error_msg.setText("empty error");
			return;
		}
		
		if(titleTextField.getText().length() > 15) {
			error_msg.setText("제목은 15자 이내로 작성해주세요.");
			return;
		}
		
		String title = titleTextField.getText();
		System.out.println(challengeTimeComboBox.getValue());
		String[] startToEnd = challengeTimeComboBox.getValue().split("~");
		String[] start = startToEnd[0].split(":");
		String[] end = startToEnd[1].split(":");
		String startTime = start[0]+start[1];
		String endTime = end[0]+end[1];
		
		Schedule schedule = new Schedule(ClientInfo.dayOfWeekList[currentDayOfWeek], title, startTime, endTime, 'C');
		tempScheduleList.add(schedule);
		ClientInfo.allChallengesCount++;
		drawSchedule(schedule);
		isChanged = true;
		timeLineListView.refresh();
		titleTextField.clear();
		hourTextField.clear();
		minTextField.clear();
	}
	@FXML public void done() {
		error_msg.setText("");
		ClientInfo.scheduleList.addAll(tempScheduleList);

		Parent second;
		try {
			second = FXMLLoader.load(getClass().getResource("templates/TimeLine.fxml"));
			second.getStylesheets().add(getClass().getResource("statics/application.css").toExternalForm());
			Scene sc = new Scene(second);
			Stage stage = (Stage)doneButton.getScene().getWindow();
			stage.setScene(sc);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	} // TODO

	@FXML public void cancel() {
		tempScheduleList.clear();
		showSchedules(ClientInfo.dayOfWeekList[currentDayOfWeek]);
		isChanged = false;
	} // TODO
	
	private void recommendTime() {
		Collections.sort(selectedScheduleList);
		
		if(selectedScheduleList.size() > 0) {
			int time1, time2;
			int userInput = Integer.parseInt(hourTextField.getText() + minTextField.getText());
			
			time1 = 0;
			time2 = selectedScheduleList.get(0).getStartTime();
			
			if(addTime(time2, -time1) >= userInput) {
				if(addTime(addTime(time2, -time1), -20) >= userInput) {
					recommendList.add(getTime(addTime(addTime(time2, -userInput), -10)) + "~" + getTime(addTime(time2, -10)));
				}
				recommendList.add(getTime(addTime(time2, -userInput)) + "~" + getTime(time2));
			}
			for(int i = 0; i < selectedScheduleList.size() - 1; i++) {
				time1 = selectedScheduleList.get(i).getEndTime();
				time2 = selectedScheduleList.get(i+1).getStartTime();
				if(addTime(time2, -time1) >= userInput) {
					if(addTime(addTime(time2, -time1), -20) >= userInput) {
						recommendList.add(getTime(addTime(addTime(time2, -userInput), -10)) + "~" + getTime(addTime(time2, -10)));
					}
					recommendList.add(getTime(addTime(time2, -userInput)) + "~" + getTime(time2));
					
					recommendList.add(getTime(time1) + "~" + getTime(addTime(time1, userInput)));
					if(addTime(addTime(time2, -time1), -20) >= userInput) {
						recommendList.add(getTime(addTime(time1, 10)) + "~" + getTime(addTime(addTime(time1, userInput), 10)));
					}
				}
			}
			time1 = selectedScheduleList.get(selectedScheduleList.size()-1).getEndTime();
			time2 = 2400;
			if(addTime(time2, -time1) >= userInput) {
				recommendList.add(getTime(time1) + "~" + getTime(addTime(time1, userInput)));
				if(addTime(addTime(time2, -time1), -20) >= userInput) {
					recommendList.add(getTime(addTime(time1, 10)) + "~" + getTime(addTime(addTime(time1, userInput), 10)));
				}
			}
		}
		
		
//		// 재헌 코드 (시작 시각을 입력하면 지속 시간 추천)
//		int startHour = Integer.parseInt(hourTextField.getText());
//		int startMin = Integer.parseInt(minTextField.getText());
//		int recommendMin = startMin;
//		for(int recommendHour = startHour; recommendHour < 24; recommendHour++) {
//			while(recommendMin < 60) {
//				if(recommendMin>=50) {
//					if(recommendHour>=23) {
//						recommendList.add(
//								startHour+":"+startMin
//								+"~23:59"
//						);
//					}else {
//						recommendList.add(
//								startHour+":"+startMin
//								+"~"+(recommendHour+1)+":"+(recommendMin-50)
//						);
//					}
//				}else {
//					recommendList.add(
//							startHour+":"+startMin
//							+"~"+recommendHour+":"+(recommendMin+10)
//					);
//				}
//				recommendMin+=10;
//			}
//			recommendMin-=60;
//		}
	}
	
	public int parseDayOfWeek(String dayOfWeek) {
		int intDayOfWeek = -1;
		switch(dayOfWeek) {
		case "일":
			intDayOfWeek = 0;
			break;
		case "월":
			intDayOfWeek = 1;
			break;
		case "화":
			intDayOfWeek = 2;
			break;
		case "수":
			intDayOfWeek = 3;
			break;
		case "목":
			intDayOfWeek = 4;
			break;
		case "금":
			intDayOfWeek = 5;
			break;
		case "토":
			intDayOfWeek = 6;
			break;
		}
		return intDayOfWeek;
	}
}
