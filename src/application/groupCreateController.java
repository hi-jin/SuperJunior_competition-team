package application;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class groupCreateController implements Initializable{
	@FXML TextField insertGroupID;
	PrintWriter out;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		try {
			out = new PrintWriter(new BufferedOutputStream(data.ClientInfo.socketInfo.getOutputStream()));
		} catch (IOException e) {
			System.out.println("서버가 닫혀있습니다.");
		}
	}
	
	@FXML
	public void createGroup() throws InterruptedException {
		String teamID = insertGroupID.getText();
		
		if(teamID.length()!=5) {
			new Alert(Alert.AlertType.ERROR, "정확하게 5자로 입력해주세요!!", ButtonType.CLOSE).show();
			return;
		}
		
		String[] teams = data.ClientInfo.groupId.split(";");
		if(teams[0].equals("null")) {
			data.ClientInfo.groupId = teamID+";";
		}else {
			data.ClientInfo.groupId += teamID+";";
		}
		out.println("group/create/"+data.ClientInfo.userId+"/"+teamID);
		out.flush();
	}
	
}
	
