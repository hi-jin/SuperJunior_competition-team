package application;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class groupGatewayController implements Initializable {
	@FXML Button createGroup;
	@FXML Button joinGroup;
	@FXML Button quitGroup;
	@FXML Button AddEssentialsButton;
	@FXML Button ShowTimeLineButton;
	@FXML Button AddChallengesButton;
	@FXML Button MoveToGroupButton;
	
	private Stage Stage;
	private PrintWriter out;
	
	public static boolean wannaQuit;
	public static String quitGroupID;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		try {
			out = new PrintWriter(new BufferedOutputStream(data.ClientInfo.socketInfo.getOutputStream()));
		} catch (IOException e) {
			System.out.println("서버가 닫혀있습니다.");
		}
		
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
				second.getStylesheets().add(getClass().getResource("statics/TimeLine.css").toExternalForm());
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
		if(data.Controllers.groupGatewayController==null) {
			data.Controllers.groupGatewayController=this;
		}
		if(wannaQuit) {
			createGroup.setDisable(true);
			createGroup.setStyle("-fx-opacity: 0");
			joinGroup.setDisable(true);
			joinGroup.setStyle("-fx-opacity: 0");
			wannaQuit = false;
		} else {
			quitGroup.setDisable(true);
			quitGroup.setStyle("-fx-opacity: 0");
		}
	}
	
	@FXML
	public void createGroupGateWay() {
		
		Stage = (Stage)createGroup.getScene().getWindow();
		Popup pop = new Popup();
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("templates/groupCreatePopup.fxml"));
			root.getStylesheets().add(getClass().getResource("statics/groupPopup.css").toExternalForm());
			// 팝업 객체에 레이아웃 추가
			pop.getContent().add(root);
			pop.setAutoHide(true); // 포커스 이동시 창 숨김
			
			// 팝업 객체에서 스테이지를 보여줌
			pop.show(Stage);
		} catch (IOException e) {
			e.printStackTrace();
		} // 저번에 설명한 경로 땡겨오기
	}
	

	
	@FXML
	public void joinGroupGateWay() {
		
		Stage = (Stage)createGroup.getScene().getWindow();
		Popup pop = new Popup();
		Parent root;
		try {
			root = FXMLLoader.load(getClass().getResource("templates/groupjoinPopup.fxml"));
			root.getStylesheets().add(getClass().getResource("statics/groupPopup.css").toExternalForm());
			// 팝업 객체에 레이아웃 추가
			pop.getContent().add(root);
			pop.setAutoHide(true); // 포커스 이동시 창 숨김
			
			// 팝업 객체에서 스테이지를 보여줌
			pop.show(Stage);
		} catch (IOException e) {
			e.printStackTrace();
		} // 저번에 설명한 경로 땡겨오기
	}
	
	@FXML
	public void quitGroup() {
		StringBuilder teams = new StringBuilder();
		Alert alert  = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setContentText(quitGroupID+"를 탈퇴하시겠습니까?");
		
		Optional<ButtonType> result = alert.showAndWait();
		if(result.get() == ButtonType.OK) {
			String[] userTeams = data.ClientInfo.groupId.split(";");
			for(String team : userTeams) {
				if(!team.equals(quitGroupID)) {
					teams.append(team+";");
				}
			}
			
			String show = teams.toString();
			if(show.equals("")) {
				show = "null;";
			}
			data.ClientInfo.groupId = show;
			
			out.println("group/quit/"+data.ClientInfo.userId+"/"+quitGroupID);
			out.flush();
		} else {
			Platform.runLater(() -> {
				new Alert(Alert.AlertType.INFORMATION, "탈퇴를 취소하셨습니다.", ButtonType.CLOSE).show();
			});
		}
	}
	
	public void moveToGroup() {
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
				groupGatewayController.wannaQuit = false;
				Parent second;
				second = FXMLLoader.load(getClass().getResource("templates/groupMain.fxml"));
				second.getStylesheets().add(getClass().getResource("statics/groupMain.css").toExternalForm());
				
				Scene sc = new Scene(second);
				Stage stage = (Stage)MoveToGroupButton.getScene().getWindow();
				try {
					Thread.sleep(250);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				stage.setScene(sc);
				stage.show();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void alertErrorMessage(String errorMsg) {
		Platform.runLater(() -> {
			new Alert(Alert.AlertType.INFORMATION, errorMsg+"\n[하단의 그룹관리를 다시 누르면 결과과 적용됩니다.]", ButtonType.CLOSE).show();
		});
		return;
	}
}
