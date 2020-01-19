package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import data.ClientInfo;
import data.Controllers;
import data.Schedule;
import data.TimeLine;
import data.TimeLineCell;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EssentialController extends TimeLine implements Initializable {

	@FXML Button next_day;
	@FXML Button pre_day;
	@FXML Button input_btn;
	@FXML Button exit;
	
	@FXML Label day;
	@FXML Label essential_num;
	@FXML Label challengesCountLabel;

	@FXML TextField start_h;
	@FXML TextField start_m;
	@FXML TextField end_h;
	@FXML TextField end_m;
	@FXML TextField title;
	@FXML Label error_msg;

	@FXML Button AddEssentialsButton;
	@FXML Button ShowTimeLineButton;
	@FXML Button AddChallengesButton;
	@FXML Button MoveToGroupButton;
	
	private int currentDayOfWeek;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Controllers.essentialController = this;
		currentDayOfWeek = 0;
		
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
				//second.getStylesheets().add(getClass().getResource("statics/application.css").toExternalForm());
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
				second.getStylesheets().add(getClass().getResource("statics/AddingChallenges.css").toExternalForm());
				Scene sc = new Scene(second);
				Stage stage = (Stage)AddChallengesButton.getScene().getWindow();
				stage.setScene(sc);
				stage.show();
			} catch(IOException e) {
				e.printStackTrace();
			}
		});
		MoveToGroupButton.setOnMouseClicked(event -> {
			
			if(data.ClientInfo.groupId.split(";")[0].equals("null")) {
				try {
					Parent second;
					second = FXMLLoader.load(getClass().getResource("templates/groupGateway.fxml"));
					second.getStylesheets().add(getClass().getResource("statics/groupGateway.css").toExternalForm());
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
					second.getStylesheets().add(getClass().getResource("statics/groupMain.css").toExternalForm());
					Scene sc = new Scene(second);
					Stage stage = (Stage)MoveToGroupButton.getScene().getWindow();
					stage.setScene(sc);
					stage.show();
				} catch(IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		day.setText(ClientInfo.dayOfWeekList[currentDayOfWeek] + "요일");
		
		if(currentDayOfWeek == 0) {
			pre_day.setDisable(true);
		}
		
		//다른요일로 넘어갔을 때 
		day.textProperty().addListener(new ChangeListener<String>() {
			@Override
			 public void changed(ObservableValue<? extends String> ov, String t, String t1) {
					
					if(currentDayOfWeek == 0) {
						pre_day.setDisable(true);
					}
					
					else {
						pre_day.setDisable(false);
					}
					
					if(currentDayOfWeek == 6) {
						next_day.setDisable(true);
					}
					else {
						next_day.setDisable(false);
					}
					
	            }
		});
		
		////////// timeLine ////////// TODO 필수일정 추가 하면 timeLine에 반영
		////////// 초기화 //////////
		timeLineListView.setFixedCellSize(cellHeight);
		timeLineList = FXCollections.observableArrayList();
		timeLineListView.setItems(timeLineList);
		///////////////////////////
		
		
		////////// 타임라인 일정 색상 표시 기능 //////////
		timeLineListView.setCellFactory(lv -> new TimeLineCell(this, selectedScheduleList));
		
		showSchedules(ClientInfo.dayOfWeekList[currentDayOfWeek]);
		///////////////////////////////////////////
		//////////////////////////////
	}
	
	@Override
	public void showSchedules(String dayOfWeek) {
		int essentialsCount = 0; // 필수일정 수
		int challengesCount = 0; // 도전일정 수
		
		clearSchedules();
		
		for(int i = 0; i < ClientInfo.scheduleList.size(); i++) {
			if(ClientInfo.scheduleList.get(i).getDayOfWeek().equals(dayOfWeek)) {
				if(ClientInfo.scheduleList.get(i).getType() == 'E')  essentialsCount++;
				else challengesCount++;
			}
			drawSchedule(ClientInfo.scheduleList.get(i));
		}
		day.setText(ClientInfo.dayOfWeekList[currentDayOfWeek] + "요일");
		essential_num.setText(essentialsCount + "");
		challengesCountLabel.setText(challengesCount + "");
	}

	
	@FXML public void next_view_btn() {
		currentDayOfWeek++;
		showSchedules(ClientInfo.dayOfWeekList[currentDayOfWeek]);
	}

	
	@FXML public void pre_view_btn() {
		currentDayOfWeek--;
		showSchedules(ClientInfo.dayOfWeekList[currentDayOfWeek]);
	}
	

	@FXML public void date_input() {

		if(title.getText().equals("") || start_h.getText().equals("") || start_m.getText().equals("")
				|| end_h.getText().equals("") || end_m.getText().equals("")) {
			error_msg.setText("empty error");
			return;
		}
		
		if(title.getText().length() > 15) {
			error_msg.setText("제목은 15자 이내로 작성해주세요.");
			return;
		}
		
		if(!start_h.getText().matches("^[0-9]{2}$") || !start_m.getText().matches("^[0-9]{2}$")
				|| !end_h.getText().matches("^[0-9]{2}$") || !end_m.getText().matches("^[0-9]{2}$")) {
			error_msg.setText("시간은 숫자 2개만 입력해주십시요. ex) 21:20");
			return;
		}
		
		if(Integer.parseInt(start_h.getText()+start_m.getText()) > Integer.parseInt(end_h.getText()+end_m.getText())) {
			error_msg.setText("시작시각이 종료시각보다 빨라야합니다!");
			return;
		}
		
		
		
		
		error_msg.setText("");
		
		//등록완료
		Schedule schedule = new Schedule(ClientInfo.dayOfWeekList[currentDayOfWeek], title.getText(), start_h.getText() + start_m.getText(), end_h.getText() + end_m.getText(), 'E');
		data.ClientInfo.scheduleList.add(schedule);
		System.out.println(schedule);
		showSchedules(ClientInfo.dayOfWeekList[currentDayOfWeek]);
		timeLineListView.refresh();
		title.clear();
		start_h.clear();
		start_m.clear();
		end_h.clear();
		end_m.clear();
	}


	@FXML public void exit() {
		try {
			System.out.println(ClientInfo.schedules);
			Parent second = FXMLLoader.load(getClass().getResource("templates/TimeLine.fxml"));
			second.getStylesheets().add(getClass().getResource("statics/application.css").toExternalForm());
			Scene sc = new Scene(second);
			Stage stage = (Stage)exit.getScene().getWindow();
			stage.setScene(sc);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
