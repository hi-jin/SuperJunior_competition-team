package application;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Vector;

import data.Schedule;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class AddingChallengesController implements Initializable {

	@FXML ListView<String> timeLineListView;
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
	private double 					cellHeight = 24;
	private ObservableList<String> 	timeLineList;
    private Vector<Schedule> 		scheduleIndexList = new Vector<>();
    
    @SuppressWarnings("serial")
	ArrayList<String> day_list = new ArrayList<String>() {{
		add("일");
		add("월");
		add("화");
		add("수");
		add("목");
		add("금");
		add("토");
	}};
	
	int day_index = 0; // day_list index
	
	StringBuilder time_info = new StringBuilder();
	StringBuilder temp_time_info = new StringBuilder();
	//////////////////////////////
	
	////////// AddChallanges //////////
	private ObservableList<String> recommendList;
	private boolean isBlank = true;
	///////////////////////////////////
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		day_index = parseDayOfWeek((new SimpleDateFormat("E", Locale.KOREA).format(new Date())));
		
		AddEssentialsButton.setOnMouseClicked(event -> {
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
		});
		
		//////////timeLine ////////// TODO 필수일정 추가 하면 timeLine에 반영
		////////// 초기화 //////////
		timeLineListView.setFixedCellSize(cellHeight);
		timeLineList = FXCollections.observableArrayList();
		timeLineListView.setItems(timeLineList);
		///////////////////////////
		
		
		////////// 타임라인 일정 색상 표시 기능 //////////
		timeLineListView.setCellFactory(lv -> new ListCell<String>() { // TODO 최적화
			@Override
			protected void updateItem(String item, boolean empty) {
				String color;
				String otherColor;
				
				Collections.sort(scheduleIndexList);
				super.updateItem(item, empty);
				if(empty) {
					setText(null);
					setStyle("");
				} else {
					setText(item);
					setStyle("");
					for(int i = 0; i < scheduleIndexList.size(); i++) {
						if(scheduleIndexList.get(i).getType() == 'E') {
							color = "#A9F5F2";
						} else {
							color = "#ebedf0";
						}
						if(scheduleIndexList.get(i).getStartIndex() < getIndex() && scheduleIndexList.get(i).getEndIndex() > getIndex()) {
							setStyle("-fx-background-color: " + color);
							break;
						} else if(scheduleIndexList.get(i).getStartIndex() == getIndex()) {
							if(i > 0) {
								if(scheduleIndexList.get(i-1).getEndIndex() == getIndex()) {
									if(scheduleIndexList.get(i-1).getType() == 'E') otherColor = "#A9F5F2";
									else otherColor = "#ebedf0";
									setStyle("-fx-background-color: linear-gradient(" + otherColor + " 50%, " + color + " 50%)");
									break;
								} 
							}
							if(getIndex() % 2 == 0) {
								setStyle("-fx-background-color: linear-gradient(-fx-control-inner-background 50%, " + color + " 50%)");
							} else {
								setStyle("-fx-background-color: linear-gradient(derive(-fx-control-inner-background, -2%) 50%, " + color + " 50%)");
							}
						} else if(scheduleIndexList.get(i).getEndIndex() == getIndex()) {
							if(i < scheduleIndexList.size()-1) {
								if(scheduleIndexList.get(i+1).getStartIndex() == getIndex()) {
									if(scheduleIndexList.get(i+1).getType() == 'E') otherColor = "#A9F5F2";
									else otherColor = "#ebedf0";
									setStyle("-fx-background-color: linear-gradient(" + color + " 50%, " + otherColor + " 50%)");
									break;
								} 
							}
							if(getIndex() % 2 == 0) {
								setStyle("-fx-background-color: linear-gradient(" + color + " 50%, -fx-control-inner-background 50%)");
							} else {
								setStyle("-fx-background-color: linear-gradient(" + color + " 50%, derive(-fx-control-inner-background, -2%) 50%)");
							}
						}
					}
				}
			}
		});
		
		showSchedules(day_list.get(day_index));
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
	
	//////////timeLine //////////
	// 요일에 맞는 타임라인을 표시함
	public void showSchedules(String dayOfWeek) {
		clearSchedules();
		String line = null;
		// TODO line = Server input
		line = data.ClientInfo.schedules; // default (테스트용)
		// TODO 서버에서 받아온 것 추가하도록 해야됨.
		// TODO 이 윗부분은 initialize 할 때 한 번에 하는 게 더 좋을 수도...
		
		String[] scheduleList = line.split("//");
		for(int i = 0; i < scheduleList.length; i++) {
			String[] command = scheduleList[i].split("/");
			
			if(command[0].equals(dayOfWeek)) {
				writeSchedule(command[1], command[2], command[3], command[4]);
			}
		}
	}
	
	// 타임라인에 스케줄 추가
	private void writeSchedule(String title, String startTime, String endTime, String type) {
		Schedule schedule = new Schedule(startTime, endTime, type.charAt(0));
		
		timeLineList.set(schedule.getStartIndex(), getTime(schedule.getStartTime()) + " " + title);
		timeLineList.set(schedule.getEndIndex(), getTime(schedule.getEndTime()));
		
		scheduleIndexList.add(schedule);
		
		for(int i = schedule.getStartIndex() + 1; i < schedule.getEndIndex(); i++) {
			timeLineList.set(i, "");
		}
	}
	
	// 타임라인 초기화
	private void clearSchedules() {
		timeLineList.clear();
		scheduleIndexList.clear();
		
		int time = 0000;
		
		timeLineList.add("");
		timeLineList.add("");
		
		for(int i = 0; i < 144; i++) {
			if(time % 100 == 0 || time % 100 == 30) {
				timeLineList.add(getTime(time));
			} else {
				timeLineList.add("");
			}
			time = addTime(time, 10);
		}
		for(int i = 0; i < 11; i++) {
			timeLineList.add("");
		}
	}
	
	private String getTime(int time) {
		int hour = time / 100;
		int min = time - hour * 100;
		
		return String.format("%02d:%02d", hour, min);
	}
	
	private int addTime(int time1, int time2) {
		int result = 0;
		
		if(time2 > 0) {
			int h1 = time1 / 100;
			int m1 = time1 % 100;
			int h2 = time2 / 100;
			int m2 = time2 % 100;
			
			while(m1 + m2 >= 60) {
				m1 -= 60;
				h1 += 1;
			}
			result = (h1 + h2) * 100 + m1 + m2;
		} else if(time2 < 0){
			time2 *= -1;
			int h1 = time1 / 100;
			int m1 = time1 % 100;
			int h2 = time2 / 100;
			int m2 = time2 % 100;

			while(m1 - m2 < 0) {
				m1 += 60;
				h1 -= 1;
			}
			result = (h1 - h2) * 100 + m1 - m2;
		} else {
			result = time1;
		}
		
		return result;
	}
	//////////////////////////////
	
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
		
		temp_time_info.append(day_list.get(day_index)+"/");
		temp_time_info.append(title+"/");
		temp_time_info.append(startTime +"/");
		temp_time_info.append(endTime+"/");
		temp_time_info.append("C//"); // Challenge (도전 일정)
		
		writeSchedule(title, startTime, endTime, "C");
		timeLineListView.refresh();
		titleTextField.clear();
		hourTextField.clear();
		minTextField.clear();
	}
	@FXML public void done() {
		time_info.append(temp_time_info.toString());
		sendChallenges();
	} // TODO

	@FXML public void cancel() {
		time_info = new StringBuilder();
		temp_time_info = new StringBuilder();

		showSchedules(day_list.get(day_index));
	} // TODO
	
	private void sendChallenges() {
		error_msg.setText("");
		
		data.ClientInfo.schedules += time_info.toString();
		time_info = new StringBuilder();
		temp_time_info = new StringBuilder();
	}
	
	private void recommendTime() {
		Collections.sort(scheduleIndexList);
		
		if(scheduleIndexList.size() > 0) {
			int time1, time2;
			int userInput = Integer.parseInt(hourTextField.getText() + minTextField.getText());
			
			time1 = 0;
			time2 = scheduleIndexList.get(0).getStartTime();
			
			if(addTime(time2, -time1) >= userInput) {
				if(addTime(addTime(time2, -time1), -20) >= userInput) {
					recommendList.add(getTime(addTime(addTime(time2, -userInput), -10)) + "~" + getTime(addTime(time2, -10)));
				}
				recommendList.add(getTime(addTime(time2, -userInput)) + "~" + getTime(time2));
			}
			for(int i = 0; i < scheduleIndexList.size() - 1; i++) {
				time1 = scheduleIndexList.get(i).getEndTime();
				time2 = scheduleIndexList.get(i+1).getStartTime();
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
			time1 = scheduleIndexList.get(scheduleIndexList.size()-1).getEndTime();
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
