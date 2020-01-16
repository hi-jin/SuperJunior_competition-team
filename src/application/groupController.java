package application;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class groupController implements Initializable {
	@FXML Button btn;
	@FXML Button AddEssentialsButton;
	@FXML Button ShowTimeLineButton;
	@FXML Button AddChallengesButton;
	@FXML Button MoveToGroupButton;
	
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
	}
}
