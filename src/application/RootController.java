package application;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import data.ClientInfo;
import data.Controllers;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;

public class RootController implements Initializable {

	Socket socket;

	@FXML TextField idTextField;

	@FXML Label error_msg;

	@FXML Button loginButton;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Controllers.rootController = this;
        
        if(ClientInfo.userId != null && !ClientInfo.userId.equals("") ) {
        	Platform.runLater(() -> idTextField.setText(ClientInfo.userId));
        	Platform.runLater(() -> login());
        }
	}

	@FXML public void login() {
		if(!idTextField.getText().equals("")) {
			loginButton.setDisable(true);
			Socket server = data.ClientInfo.socketInfo;
			try {
				PrintWriter out = new PrintWriter(new BufferedOutputStream(server.getOutputStream()));
				out.println("login/" + idTextField.getText());
				out.flush();
				Thread.sleep(1000);
				if(ClientInfo.userId != null && !ClientInfo.userId.equals("")) {
					Parent second = FXMLLoader.load(getClass().getResource("templates/first.fxml"));
					second.getStylesheets().add(getClass().getResource("statics/application.css").toExternalForm());
					Scene sc = new Scene(second);
					Stage stage = (Stage)loginButton.getScene().getWindow();
					stage.setScene(sc);
					stage.show();
				} else {
		        	error_msg.setText("아이디를 다시 입력해주세요.");
					loginButton.setDisable(false);
				}
			} catch (IOException e) {
	        	error_msg.setText("서버 에러 혹은 인터넷 연결을 확인해주세요.");
	        	loginButton.setDisable(true);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}