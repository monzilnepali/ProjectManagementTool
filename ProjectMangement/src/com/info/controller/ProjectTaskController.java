package com.info.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProjectTaskController implements Initializable {

	
	 @FXML   private TableView<?> taskTable;
	 @FXML	 private Button AddTaskBtn;
	 private static int currentProjectId;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		System.out.println("project Task controller calleld");
		
		
		AddTaskBtn.setOnAction(e->{
			System.out.println("add task button clicked");
			//opening addtask scene 
			FXMLLoader loader=new FXMLLoader();
			loader.setLocation(getClass().getResource("/application/TaskAdd.fxml"));
			try {
				loader.load();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			TaskAddController controller=loader.getController();
			controller.setData(currentProjectId);
			Parent p=loader.getRoot();
			Scene taskAddScene=new Scene(p);
			Stage taskAddStage=new Stage();
			Stage previousStage=(Stage)((Node)e.getSource()).getScene().getWindow();
			taskAddStage.setScene(taskAddScene);
			taskAddStage.setTitle("Add Task");
			taskAddStage.initModality(Modality.WINDOW_MODAL);
			taskAddStage.initOwner(previousStage);
			taskAddStage.setResizable(false);
			taskAddStage.show();
			
			
		
			
			
		});
		
	}
	public void setData(String CurrentProjectId) {
		
		currentProjectId=Integer.valueOf(CurrentProjectId);
	}

}
