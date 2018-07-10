package com.info.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.info.dao.ProjectDao;
import com.info.model.Task;
import com.jfoenix.controls.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class TaskStatusUpdateController implements Initializable {
	@FXML private Label projectName;

	@FXML private Label taskTitle;

	@FXML
	private Label taskDesc;

	@FXML
	private Button docsDownloadBtn;

	@FXML
	private ListView<String> docsList;

	@FXML
	private Label taskAssignDate;

	@FXML
	private Label taskDeadlineDate;
	private List<String > list=new ArrayList<String>();
	ObservableList<String> olist;
	
    static CurrentUserSingleton tmp=CurrentUserSingleton.getInstance();	//current user object


	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		System.out.println("task status update called");
	}
	public void setData(Task task, ProjectTaskController controller) {
		System.out.println("set data task status update called");
		// filling data in form
		
		//getting the name of project from server
		System.out.println("the project id"+task.getProjectId());
		String projectname=ProjectDao.getProjectNameThroughId(task.getProjectId());
		//getting the list of all  file of that task via task id
		System.out.println("the project name s "+projectname);
		System.out.println("task ttike"+task.getTaskId());
		
		
		projectName.setText(projectname);
		taskDeadlineDate.setText(task.getTaskDeadLine());
		taskTitle.setText(task.getTaskName());
		
		list=ProjectDao.getTaskFileThroughId(task.getTaskId());
		for(String st:list) {
			System.out.println("the file "+st);
		}
		olist=FXCollections.observableArrayList(list);

		docsList.setItems(olist);
	
		
		
		docsDownloadBtn.setOnAction(e->{
			//downloading task docs started
			
			//requesting server to download the docs of task using task id
			tmp.getOut().println("downloadTaskNote");
			tmp.getOut().println(task.getTaskId());
			tmp.getOut().print(projectname);
		
			
			
			
			
		});
		
		
		
		
		
		

	}

}
