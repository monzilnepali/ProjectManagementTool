package com.info.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.info.dao.ProjectDao;
import com.info.model.TaskModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class TaskCompletionController implements Initializable {
	
	@FXML
    private Label taskName;

    @FXML
    private Label taskAssignUserName;

    @FXML
    private ListView<String> taskFileList;

    @FXML
    private Button FileDownload;

    @FXML
    private Button MarkCompletedBtn;
    ObservableList<String> oblist;
    static CurrentUserSingleton tmp = CurrentUserSingleton.getInstance(); 
	
	public void setData(TaskModel newTask,ProjectTaskController ptController) {
		System.out.println("the project task id"+newTask.getProjectId());
		
		taskName.setText(newTask.getTaskName());
		taskAssignUserName.setText(newTask.getTaskAssignToName());
		
		
		//getting list of  file of task from the database
		List<String> fileList =ProjectDao.getCompletedTask(newTask.getTaskId());
		oblist=FXCollections.observableArrayList(fileList);
		taskFileList.setItems(oblist);
		
		
		
		
		MarkCompletedBtn.setOnAction(e->{
			System.out.println("mark completed btn");
			
			//marking that project as completed
			if(ProjectDao.MarkTaskCompleted(newTask.getTaskId())) {
				 	System.out.println("task marked as completed");
				//marking task as completed
				tmp.getOut().println("SendEmailToCompletedTeam");
				tmp.getOut().println(newTask.getTaskId());
				
			}else {
			
			}
			
			
			
			
		});
		
	}


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	

}
