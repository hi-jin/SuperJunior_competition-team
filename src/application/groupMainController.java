package application;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
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
	private ObservableList<String> rankList = FXCollections.observableArrayList();
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
			
			out.println("progress/getUser/"+data.ClientInfo.userId);
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
		out.println("progress/getGroup/"+teams[0]);
		out.flush();
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
		rankList.clear();
		
		String[] userInfo = groupRank.split("//");
		for(String user : userInfo) {
			rankList.add(user);
		}
		
		Collections.sort(rankList, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				String[] o1Info = o1.split("/");
				String[] o2Info = o2.split("/");
				int o1Progress = Integer.parseInt(o1Info[1]);
				int o2Progress = Integer.parseInt(o2Info[1]);
				if(o1Progress > o2Progress) {
					return 1;
				} else if (o1Progress == o2Progress) {
					return 0;
				} else {
					return -1;
				}
			}
			
		});
		
		this.groupRank.setItems(rankList);
	}

}
