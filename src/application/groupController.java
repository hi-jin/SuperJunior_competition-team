package application;


import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class groupController {
	@FXML Button btn;
	private Stage Stage;
	
	public void createGroup() {
		Stage = (Stage)btn.getScene().getWindow();
		Popup pop = new Popup();
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("popup.fxml"));
			// 팝업 객체에 레이아웃 추가
			pop.getContent().add(root);
			pop.setAutoHide(true); // 포커스 이동시 창 숨김
			
			// 팝업 객체에서 스테이지를 보여줌
			pop.show(Stage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 저번에 설명한 경로 땡겨오기
	}
	
	public void joinGroup() {
		Stage = (Stage)btn.getScene().getWindow();
		Popup pop = new Popup();
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("popup.fxml"));
			// 팝업 객체에 레이아웃 추가
			pop.getContent().add(root);
			pop.setAutoHide(true); // 포커스 이동시 창 숨김
			
			// 팝업 객체에서 스테이지를 보여줌
			pop.show(Stage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 저번에 설명한 경로 땡겨오기
	}
}
