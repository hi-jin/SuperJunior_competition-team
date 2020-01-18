package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import data.ClientInfo;
import javafx.application.Platform;

public class ServerListener extends Thread {

	@Override
	public void run() {
        try {
			BufferedReader in = new BufferedReader(new InputStreamReader(data.ClientInfo.socketInfo.getInputStream()));
			
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
						ClientInfo.groupId = command[2]+";";
					} else if(command[1].equals("2")) {
						Platform.runLater(() -> data.Controllers.rootController.login());
					} else {
						Platform.runLater(() -> data.Controllers.rootController.error_msg.setText("다시 시도해주세요."));
					}
					break;
				case "update":
					try {
						data.Controllers.timeLineController.nextDay();
					} catch (Exception e) {}
					break;
				case "group":
					if(command[1].equals("error")) {
						data.Controllers.groupGatewayController.alertErrorMessage(command[2]);
					}
					break;	
				case "progress":
					if(command[1].equals("getUser")) {
						data.Controllers.groupMainController.setProgress(command[2]);
					}
					if(command[1].equals("getGroup")) {
						data.Controllers.groupMainController.setRank(command[2]);
					}
					break;
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
