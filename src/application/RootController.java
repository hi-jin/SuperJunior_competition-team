package application;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class RootController implements Initializable {

	@FXML Label serverTest;
	
	Socket socket;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		if(!ClientInfo.connection) { //socket connect를 최초 실행시 한 번만 실행되기 위함 , 소켓  객체 중복 생성 방지
			
		final String SERVER_IP = "172.30.1.14"; //local에서 테스트 중일 경우 자신의 IP 작성

		final int SERVER_PORT = 50000;
		
        socket = new Socket();
        
        try {
            socket.connect( new InetSocketAddress(SERVER_IP, SERVER_PORT) );
            serverTest.setText("success connection to server"); //test code
            
            ClientInfo.setSocket(socket);
        }
        
        catch (IOException e) {
            e.printStackTrace();
        }
		
	}

}
}