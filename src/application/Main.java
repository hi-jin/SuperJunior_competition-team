package application;
	
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;

import data.ClientInfo;
import data.FileIO;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("templates/main.fxml"));
			Parent root = loader.load();
			root.getStylesheets().add(getClass().getResource("statics/application.css").toExternalForm());
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
			primaryStage.setResizable(false);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		FileIO.read();
		
		if(!ClientInfo.connection) { //socket connect를 최초 실행시 한 번만 실행되기 위함 , 소켓  객체 중복 생성 방지
			
			final String SERVER_IP = "localhost"; //local에서 테스트 중일 경우 자신의 IP 작성

			final int SERVER_PORT = 40000;
			
	        ClientInfo.socketInfo = new Socket();
	        
	        Socket socket = ClientInfo.socketInfo;
	        
	        try {
	            socket.connect( new InetSocketAddress(SERVER_IP, SERVER_PORT) );
	            
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
									ClientInfo.userId = data.Controllers.rootController.idTextField.getText();
									ClientInfo.groupId = command[2];
								} else if(command[1].equals("2")) {
									Platform.runLater(() -> data.Controllers.rootController.login());
								} else {
									Platform.runLater(() -> data.Controllers.rootController.error_msg.setText("다시 시도해주세요."));
								}
								break;
							case "update":
								// TODO 진척도를 업데이트 하도록 시킴.
								break;
							}
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
	            });
	            listener.setDaemon(true);
	            listener.start();
	        }
	        
	        catch (IOException e) {
				e.printStackTrace();
	        }
		}
		
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			FileIO.write();
			System.out.println("저장 완료");
		}));
		launch(args);
	}
}
