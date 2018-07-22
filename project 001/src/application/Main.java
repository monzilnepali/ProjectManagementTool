package application;
	
import java.util.Date;

import com.info.controller.CurrentUserSingleton;
import com.info.dao.UserDao;
import com.info.utils.DBConnection;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {
	 static CurrentUserSingleton tmp=CurrentUserSingleton.getInstance();	//current user object
	@Override
	public void start(Stage primaryStage) {
		try {
			tmp.setStage(primaryStage);
			
			System.out.println("current ttime"+UserDao.formattedDate(new Date()));
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
