package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("templates/AddingChallenges.fxml"));
//			FXMLLoader loader = new FXMLLoader(getClass().getResource("templates/first.fxml"));
//			FXMLLoader loader = new FXMLLoader(getClass().getResource("templates/TimeLine.fxml"));
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
		launch(args);
	}
}
