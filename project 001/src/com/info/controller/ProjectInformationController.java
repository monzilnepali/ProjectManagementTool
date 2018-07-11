package com.info.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.info.dao.ProjectDao;
import com.info.model.Project;
import com.jfoenix.controls.JFXProgressBar;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class ProjectInformationController implements Initializable {

	
	@FXML private Label projectNameField;
    @FXML private Label projectDescriptionField;
    @FXML private Label projectCreationDate;
    @FXML private Label projectCategories;
    @FXML private Button docsDownloadBtn;
    @FXML  private JFXProgressBar downloadProgressBar;
    @FXML private ListView<String> ProjectDocsList;
    ObservableList<String> olist;
    
    
    static CurrentUserSingleton tmp=CurrentUserSingleton.getInstance();	//current user object
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		docsDownloadBtn.setVisible(false);
		downloadProgressBar.setVisible(false);
		System.out.println("project information controller called");
		
		
		
	}
	public void setData(int projectid,String role)  {
		
		System.out.println("the role is   ->>"+role);
		//if user is manager then download btn will be hidden
		//team member can download the project file 
		
	if(role.equals(" Manager")) {
		docsDownloadBtn.setVisible(false);
	}else {
		docsDownloadBtn.setVisible(true);
	}
		
		
		//getting data about project
		Project projectInfo=ProjectDao.getProjectInformation(projectid);
		projectNameField.setText(projectInfo.getprojectTitle());
		projectDescriptionField.setText(projectInfo.getProjectDesc());
		projectCategories.setText(projectInfo.getCategories());
		projectCreationDate.setText(projectInfo.getProjectCreationDate());
		
		//setting listview with the projectdocs
		
		
		olist=FXCollections.observableArrayList(ProjectDao.getProjectFileThroughProjectId(projectid));
		ProjectDocsList.setItems(olist);
		
		docsDownloadBtn.setOnAction(e->{
			//downloading the documentation of project
			//create new folder in user device 
			//requesting server 
			System.out.println("button clicked");
			tmp.getOut().println("docsDownload");
			//sending the project id 
			tmp.getOut().println(projectid);
		
			
			
			
		});
		
	}
	public static void showDialog(String filePath) {
		 Alert alert = new Alert(AlertType.INFORMATION);
	        alert.setTitle("Download Completed");
	      
	        alert.setContentText("File location: "+filePath);
	 
	        alert.showAndWait();
	}

}
