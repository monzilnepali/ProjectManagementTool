package application;
	
import com.info.utils.DBConnection;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent p=FXMLLoader.load(getClass().getResource("Login_window.fxml"));
			Scene scene = new Scene(p);
			System.out.println("width"+primaryStage.getMaxHeight());
			String css1=this.getClass().getResource("application.css").toExternalForm();
			String css2=this.getClass().getResource("Login_window.css").toExternalForm();
			scene.getStylesheets().addAll(css1,css2);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Project Management");
			primaryStage.setResizable(false);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		DBConnection.getConnection();
		launch(args);
	}
}
