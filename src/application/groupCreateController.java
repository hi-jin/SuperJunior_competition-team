package application;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

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
	public void createGroup() {
		String groupID = insertGroupID.getText();
		
		if(groupID.length()!=5) {
			new Alert(Alert.AlertType.ERROR, "정확하게 5자로 입력해주세요!!", ButtonType.CLOSE).show();
			return;
		}
		out.println("group/create/"+groupID);
		out.flush();
	}
	
}
