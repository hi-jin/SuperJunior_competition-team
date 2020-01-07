package application;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

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
	
	
	@SuppressWarnings("serial")
	ArrayList<String> day_list = new ArrayList<String>() {{
		add("일요일");
		add("월요일");
		add("화요일");
		add("수요일");
		add("목요일");
		add("금요일");
		add("토요일");
	}};
	int day_index = 0; // day_list index
	
	int[] essential = {0, 0, 0, 0, 0, 0, 0}; //요일별 필수 일정 개수
	
	StringBuilder time_info = new StringBuilder();
	@FXML Label error_msg;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
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
	}
	

	
	@FXML public void next_view_btn() {
		
		day_index ++;
		day.setText(day_list.get(day_index));
	}

	
	@FXML public void pre_view_btn() {

		day_index --;
		day.setText(day_list.get(day_index));
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
		time_info.append(end_m.getText()+"//");
		
		
		//등록완료
		title.clear();
		start_h.clear();
		start_m.clear();
		end_h.clear();
		end_m.clear();
		essential[day_index] += 1;
		essential_num.setText(String.valueOf(essential[day_index]));
	}


	@FXML public void exit() {
		
		System.out.println(time_info);
	}

}
