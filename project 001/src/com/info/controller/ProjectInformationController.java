package com.info.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.info.controller.CurrentUserSingleton;
import com.info.dao.ProjectDao;
import com.info.model.Project;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

public class ProjectInformationController implements Initializable {

   static CurrentUserSingleton tmp=CurrentUserSingleton.getInstance();
   
   @FXML
   private Label projectName;

   @FXML
   private Label projectManager;

   @FXML
   private Label projectCreationDate;

   @FXML
   private Label projectCategories;
   
   @FXML private Pane infoPane;
   @FXML  private ListView<String> ProjectDocsList;
   @FXML private Button docsDownloadBtn;
   

   ObservableList<String> olist;
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        System.out.println("the current project id is "+tmp.getActiveProjectId());
        //getting project informatiom
        
        Project currentProject=ProjectDao.getProjectInformation(tmp.getActiveProjectId());
        projectName.setText(currentProject.getprojectTitle());
        projectManager.setText(ProjectDao.getProjectManagerViaProjectId(tmp.getActiveProjectId()));
        projectCreationDate.setText(currentProject.getProjectCreationDate());
        projectCategories.setText(currentProject.getCategories());
        
        
        System.out.println("the user role in this project is "+tmp.getCurrentUserRoleInActiveProject());
        olist=FXCollections.observableArrayList(ProjectDao.getProjectFileThroughProjectId(tmp.getActiveProjectId()));
        ProjectDocsList.setItems(olist);
        
     
        
        docsDownloadBtn.setOnAction(e->{
            //downloading the documentation of project
            //create new folder in user device 
            //requesting server 
            System.out.println("button clicked");
            tmp.getOut().println("docsDownload");
            //sending the project id 
            tmp.getOut().println(tmp.getActiveProjectId());
 
        });
        
        
    }

}
