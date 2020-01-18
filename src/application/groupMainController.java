package application;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.ResourceBundle;

import data.ClientInfo;
import data.GroupCell;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
	private ObservableList<String> showList = FXCollections.observableArrayList();
	private static PrintWriter out;
	
	public static String progress;
	public static String groupRankInfo;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		if(data.Controllers.groupMainController==null) {
			data.Controllers.groupMainController = this;
			
			try {
				out = new PrintWriter(new BufferedOutputStream(data.ClientInfo.socketInfo.getOutputStream()));
			} catch (IOException e) {
				System.out.println("서버가 닫혀있습니다.");
			}
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
		
		try {
			setRank(teams[0]);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		groupRank.setCellFactory(showList -> new GroupCell());

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
	
	@FXML
	public void showTeam(ActionEvent event) throws InterruptedException {
		setRank(myTeam.getValue());
	}
	
	public void setRank(String groupID) throws InterruptedException {
		rankList.clear();
		showList.clear();
		out.println("progress/getGroup/"+groupID);
		out.flush();
		
		Thread.sleep(200);
		String[] userInfo = groupRankInfo.split(";;");
		for(String user : userInfo) {
			String[] info = user.split(";");
			if(info[0].equals(ClientInfo.userId)) {
				progressOfMonth.setText(info[1]);
			}
			rankList.add(user);
		}
		
		Collections.sort(rankList, new Comparator<String>() {

			@Override
			public int compare(String o1, String o2) {
				String[] o1Info = o1.split(";");
				String[] o2Info = o2.split(";");
				int o1Progress = Integer.parseInt(o1Info[1].split("%")[0]);
				int o2Progress = Integer.parseInt(o2Info[1].split("%")[0]);
				if(o1Progress < o2Progress) {
					return 1;
				} else if (o1Progress == o2Progress) {
					return 0;
				} else {
					return -1;
				}
			}
			
		});
		
		int rank = 1;
		for(String user : rankList) {
			String[] info = user.split(";");
			if(info[0].equals(ClientInfo.userId)) {		
				myRank.setText(rank+"등");
			}
			rank++;
		}
		for(int i=0; i < rankList.size(); i++) {
			showList.add((i+1)+"등");
			String[] info = rankList.get(i).split(";");
			showList.add(info[0]);
			showList.add(info[1]);
		}

		this.groupRank.setItems(showList);
	}

}
