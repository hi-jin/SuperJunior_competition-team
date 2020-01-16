package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Vector;

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
	@FXML ComboBox challengeTimeComboBox; // TODO ComboBox + Algorithm
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
    private Vector<Integer[]> 		scheduleIndexList = new Vector<>();
    
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
		
		//////////timeLine ////////// TODO 필수일정 추가 하면 timeLine에 반영
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
	
	////////// timeLine //////////
	// 요일에 맞는 타임라인을 표시함
	public void showSchedules(String dayOfWeek) {
		clearSchedules();
		String line = null;
		// TODO line = Server input
		
		time_info.append("일/브베 배 빵빵 연습하기/2000/2040//일/앵망 빵됴 연습하기/2100/2120//"
			+ "일/앵망 빵듀데요 연습하기/2200/2240//월/박으수/1000/1030");
		line = time_info.toString(); // default (테스트용)
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

	@FXML public void done() {
		sendChallenges();
	} // TODO

	@FXML public void addMore() {
		sendChallenges();
	} // TODO

	@FXML public void cancel() {
		sendChallenges();
	} // TODO
	
	private void sendChallenges() {
		if(titleTextField.getText().equals("") || hourTextField.getText().equals("") || minTextField.getText().equals("")) {
			error_msg.setText("empty error");
			return;
		}
		
		if(titleTextField.getText().length() > 15) {
			error_msg.setText("제목은 15자 이내로 작성해주세요.");
			return;
		}
		
		if(!hourTextField.getText().matches("^[0-9]{2}$") || !minTextField.getText().matches("^[0-9]{2}$")) {
			error_msg.setText("시간은 숫자 2개만 입력해주십시요. ex) 21:20");
			return;
		}
		
		error_msg.setText("");
	}
}
