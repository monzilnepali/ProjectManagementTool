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
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

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
	
	public void setData(TaskModel newTask,ProjectTaskController controller) {
		System.out.println("the project task id"+newTask.getProjectId());
		
		taskName.setText(newTask.getTaskName());
		taskAssignUserName.setText(newTask.getTaskAssignToName());
		
		
		//getting list of  file of task from the database
		List<String> fileList =ProjectDao.getCompletedTask(newTask.getTaskId());
		oblist=FXCollections.observableArrayList(fileList);
		taskFileList.setItems(oblist);
		
		FileDownload.setOnAction(e -> {
	            // downloading task docs started

	            // requesting server to download the docs of task using task id
	            tmp.getOut().println("downloadTaskNote");
	            tmp.getOut().println(newTask.getTaskId());
	            tmp.getOut().println(newTask.getProjectId());
	            

	        });
		
		
		MarkCompletedBtn.setOnAction(e->{
			System.out.println("mark completed btn");
			
			tmp.getOut().println("updateTaskStatus");
            //true then update task status 
            tmp.getOut().println(newTask.getTaskId());
            tmp.getOut().println(3);
            
            //gettting current stage
            Stage stage=(Stage)((Node)e.getSource()).getScene().getWindow();
            stage.close();
            controller.removeTask(newTask.getTaskId());
          
			
			
			
		});
		
	}


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	

}
