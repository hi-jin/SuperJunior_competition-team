package application;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class groupMainController implements Initializable{
	@FXML ComboBox<String> myTeam;
	@FXML Button AddEssentialsButton;
	@FXML Button ShowTimeLineButton;
	@FXML Button AddChallengesButton;
	@FXML Button MoveToGroupButton;
	@FXML Label progressOfMonth;
	@FXML Label myRank;
	@FXML ListView<String> groupRank;
	
	private ObservableList<String> teamList;
	private ObservableList<String> rankList;
	private static PrintWriter out;
	private static String progress;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		if(data.Controllers.groupMainController==null) {
			data.Controllers.groupMainController = this;
			
			try {
				out = new PrintWriter(new BufferedOutputStream(data.ClientInfo.socketInfo.getOutputStream()));
			} catch (IOException e) {
				System.out.println("서버가 닫혀있습니다.");
			}
			
			out.println("progress/"+data.ClientInfo.userId);
			out.flush();
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
			if(data.ClientInfo.groupId.equals("null")) {
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
		

		
		teamList = FXCollections.observableArrayList();
		String[] teams = data.ClientInfo.groupId.split(";");
		for(String team : teams) {
			teamList.add(team);
		}
		myTeam.setItems(teamList);
		Platform.runLater(() -> {
			myTeam.setPromptText(teams[0]);
		});
		rankList = FXCollections.observableArrayList();
		groupRank.setItems(rankList);
		
		Platform.runLater(() -> {
			progressOfMonth.setText(progress);
		});
	}
	
	@FXML
	public void moveToCreateOrJoinGroup() {
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
	}
	
	public void setProgress(String progress) {
		groupMainController.progress = progress;
	}
	
	public void setRank(String groupRank) {
		// TODO
	}

}
