package com.info.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.info.model.ProjectFile;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

public class ProjectFileDetailsController implements Initializable{
    
    
    @FXML private Label taskName;

    @FXML private TextArea taskDesc;

    @FXML private ListView<String> taskDocs;

    @FXML private Button taskDocsDownload;

    @FXML private TextArea taskSummary;

    @FXML private ListView<String> taskFile;

    @FXML private Button taskFileDownload;
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
       
        System.out.println("project file details controller called");
    }

    public void setdata(ProjectFile pf) {
        
        //getting task details
       
        
    }

}
