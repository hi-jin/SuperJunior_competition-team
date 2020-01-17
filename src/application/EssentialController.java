package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Vector;

import data.ClientInfo;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.ListView;

public class EssentialController implements Initializable {

	@FXML Button next_day;
	@FXML Button pre_day;
	@FXML Button input_btn;
	@FXML Button exit;
	
	@FXML Label day;
	@FXML Label essential_num;

	@FXML TextField start_h;
	@FXML TextField start_m;
	@FXML TextField end_h;
	@FXML TextField end_m;
	@FXML TextField title;
	@FXML Label error_msg;
	@FXML ListView<String> timeLineListView;

	@FXML Button AddEssentialsButton;
	@FXML Button ShowTimeLineButton;
	@FXML Button AddChallengesButton;
	@FXML Button MoveToGroupButton;
	
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
	
	int[] essential = {0, 0, 0, 0, 0, 0, 0}; //요일별 필수 일정 개수
	
	StringBuilder time_info = new StringBuilder();
	
	////////// timeLine //////////
	private double 					cellHeight = 24;
	private ObservableList<String> 	timeLineList;
    private Vector<Integer[]> 		scheduleIndexList = new Vector<>();
	//////////////////////////////
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		AddEssentialsButton.setOnMouseClicked(event -> {
			try {
				Parent second;
				second = FXMLLoader.load(getClass().getResource("templates/first.fxml"));
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
				Scene sc = new Scene(second);
				Stage stage = (Stage)MoveToGroupButton.getScene().getWindow();
				stage.setScene(sc);
				stage.show();
			} catch(IOException e) {
				e.printStackTrace();
			}
		});
		
		day.setText(day_list.get(day_index));
		essential_num.setText(String.valueOf(essential[day_index]));
		
		if(day_index == 0) {
			pre_day.setDisable(true);
		}
		
		//다른요일로 넘어갔을 때 
		day.textProperty().addListener(new ChangeListener<String>() {
			@Override
			 public void changed(ObservableValue<? extends String> ov, String t, String t1) {
					
				essential_num.setText(String.valueOf(essential[day_index]));
					
					if(day_index == 0) {
						pre_day.setDisable(true);
					}
					
					else {
						pre_day.setDisable(false);
					}
					
					if(day_index == 6) {
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
		timeLineListView.setCellFactory(lv -> new ListCell<String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if(empty) {
					setText(null);
					setStyle("");
				} else {
					setText(item);
					setStyle("");
					for(int i = 0; i < scheduleIndexList.size(); i++) {
						if(scheduleIndexList.get(i)[0] < getIndex() && scheduleIndexList.get(i)[1] > getIndex()) {
							setStyle("-fx-background-color: #A9F5F2");
						} else if(scheduleIndexList.get(i)[0] == getIndex()) {
							if(getIndex() % 2 == 0) {
								setStyle("-fx-background-color: linear-gradient(-fx-control-inner-background 50%, #A9F5F2 50%)");
							} else {
								setStyle("-fx-background-color: linear-gradient(derive(-fx-control-inner-background, -2%) 50%, #A9F5F2 50%)");
							}
						} else if(scheduleIndexList.get(i)[1] == getIndex()) {
							if(getIndex() % 2 == 0) {
								setStyle("-fx-background-color: linear-gradient(#A9F5F2 50%, -fx-control-inner-background 50%)");
							} else {
								setStyle("-fx-background-color: linear-gradient(derive(#A9F5F2, -2%) 50%, -fx-control-inner-background 50%)");
							}
						}
					}
				}
			}
		});
		
		showSchedules(day_list.get(day_index));
		///////////////////////////////////////////
		//////////////////////////////
	}
	

	
	@FXML public void next_view_btn() {
		
		day_index ++;
		day.setText(day_list.get(day_index) + "요일");
		showSchedules(day_list.get(day_index));
	}

	
	@FXML public void pre_view_btn() {

		day_index --;
		day.setText(day_list.get(day_index) + "요일");
		showSchedules(day_list.get(day_index));
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

		time_info.append(day_list.get(day_index)+"/");
		time_info.append(title.getText()+"/");
		time_info.append(start_h.getText());
		time_info.append(start_m.getText()+"/");
		time_info.append(end_h.getText());
		time_info.append(end_m.getText()+"/");
		time_info.append("E//"); // Essential (필수 일정)
		
		
		//등록완료
		title.clear();
		start_h.clear();
		start_m.clear();
		end_h.clear();
		end_m.clear();
		essential[day_index] += 1;
		essential_num.setText(String.valueOf(essential[day_index]));
		data.ClientInfo.schedules += time_info.toString();
		showSchedules(day_list.get(day_index));
	}


	@FXML public void exit() {
		try {
			System.out.println(ClientInfo.schedules);
			Parent second = FXMLLoader.load(getClass().getResource("templates/TimeLine.fxml"));
			Scene sc = new Scene(second);
			Stage stage = (Stage)exit.getScene().getWindow();
			stage.setScene(sc);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	////////// timeLine //////////
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
				writeSchedule(command[1], command[2], command[3]);
			}
		}
	}

	// 타임라인에 스케줄 추가
	private void writeSchedule(String title, String startTime, String endTime) {
		int time = Integer.parseInt(startTime);
		int hour = time / 100;
		int min = time - hour * 100;
		int startIndex = min / 10 + hour * 6 + 2;
		timeLineList.set(startIndex, String.format("%02d:%02d", hour, min) + " " + title);
		
		time = Integer.parseInt(endTime);
		hour = time / 100;
		min = time - hour * 100;
		int endIndex = min / 10 + hour * 6 + 2;
		timeLineList.set(endIndex, String.format("%02d:%02d", hour, min));
		
		Integer[] indexList = {startIndex, endIndex};
		scheduleIndexList.add(indexList);
		for(int i = startIndex + 1; i < endIndex; i++) {
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
	
	private int addTime(int time, int min) {
		if(time - (time / 100) * 100 > 49) {
			time -= 50;
			time += 100;
		} else {
			time += 10;
		}
		
		return time;
	}
	//////////////////////////////

}
