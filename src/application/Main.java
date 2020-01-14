package application;
	
import data.ClientInfo;
import data.FileIO;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FileIO.read();
			FXMLLoader loader = null;
			if(ClientInfo.userId.equals("")) {
				loader = new FXMLLoader(getClass().getResource("templates/main.fxml"));
			} else {
				loader = new FXMLLoader(getClass().getResource("templates/TimeLine.fxml")); // TODO 로그인 후 첫화면이 뭐지?
			}
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
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			FileIO.write();
			System.out.println("저장 완료");
		}));
		launch(args);
	}
}
