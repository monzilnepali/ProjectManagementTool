package com.info.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.info.dao.ProjectDao;
import com.jfoenix.controls.JFXRadioButton;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class TaskStatusUpdateController implements Initializable {

	 @FXML
	    private JFXRadioButton inProgressStatus;
	    @FXML
	    private JFXRadioButton finishedStatus;

	    @FXML
	    private Button taskStatusBtn;
	    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		
	}
	public void setData(int task_id,ProjectTaskController controller) {
		System.out.println("set data task status update called");
		
		inProgressStatus.setOnAction(e->{
			finishedStatus.setSelected(false);
		});
		finishedStatus.setOnAction(e->{
			inProgressStatus.setSelected(false);
		});
		
		taskStatusBtn.setOnAction(e->{
			String status;
			if(inProgressStatus.isSelected()) {
			status="in progress";
			}else {
				status="finished ";
			}
			
			if(ProjectDao.updateTaskStatus(task_id, status)) {
				
				Stage stage=(Stage)((Node)e.getSource()).getScene().getWindow();
				controller.loadData();
				stage.close();
			}
			
		});
	}

}
