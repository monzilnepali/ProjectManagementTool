package com.info.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.info.dao.ProjectDao;
import com.info.model.Project;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class ProjectInformationController implements Initializable {

	
	@FXML private Label projectNameField;
    @FXML private Label projectDescriptionField;
    @FXML private Label projectCreationDate;
    @FXML private Label projectCategories;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		System.out.println("project information controller called");
	}
	public void setData(int projectid) {
		
		//getting data about project
		Project projectInfo=ProjectDao.getProjectInformation(projectid);
		projectNameField.setText(projectInfo.getprojectTitle());
		projectDescriptionField.setText(projectInfo.getProjectDesc());
		projectCategories.setText(projectInfo.getCategories());
		projectCreationDate.setText(projectInfo.getProjectCreationDate());
		
	}

}
