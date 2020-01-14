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
		
		if(!ClientInfo.connection) { //socket connect를 최초 실행시 한 번만 실행되기 위함 , 소켓  객체 중복 생성 방지
			
		final String SERVER_IP = "localhost"; //local에서 테스트 중일 경우 자신의 IP 작성

		final int SERVER_PORT = 50000;
		
        socket = new Socket();
        
        try {
            socket.connect( new InetSocketAddress(SERVER_IP, SERVER_PORT) );
            error_msg.setText("success connection to server"); //test code
            
            ClientInfo.setSocket(socket);
            
            Thread listener = new Thread(() -> {
            	try {
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					
					String line;
					String[] command;
					while(true) {
						line = in.readLine();
						System.out.println("server=>" + line);
						
						command = line.split("/");
						
						switch(command[0]) {
						case "login":
							if(command[1].equals("1")) {
								ClientInfo.userId = idTextField.getText();
								ClientInfo.groupId = command[2];
								Platform.runLater(() -> error_msg.setText("로그인 성공."));
							} else if(command[1].equals("2")) {
								login();
							} else {
								Platform.runLater(() -> error_msg.setText("다시 시도해주세요."));
							}
						}
					}
				} catch (IOException e) {
					Platform.runLater(() -> error_msg.setText("서버 에러 혹은 인터넷 연결을 확인해주세요."));
					Platform.runLater(() -> loginButton.setDisable(true));
				}
            });
            listener.setDaemon(true);
            listener.start();
        }
        
        catch (IOException e) {
        	Platform.runLater(() -> error_msg.setText("서버 에러 혹은 인터넷 연결을 확인해주세요."));
        	Platform.runLater(() -> loginButton.setDisable(true));
        }
		
	}

}

	@FXML public void login() {
		if(!idTextField.getText().equals("")) {
			Socket server = data.ClientInfo.socketInfo;
			try {
				PrintWriter out = new PrintWriter(new BufferedOutputStream(server.getOutputStream()));
				out.println("login/" + idTextField.getText());
				out.flush();
				Thread.sleep(400);
				if(!ClientInfo.userId.equals("")) {
					Parent second = FXMLLoader.load(getClass().getResource("templates/first.fxml"));
					Scene sc = new Scene(second);
					Stage stage = (Stage)loginButton.getScene().getWindow();
					stage.setScene(sc);
					stage.show();
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