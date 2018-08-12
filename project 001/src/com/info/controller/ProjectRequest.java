package com.info.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ProjectRequest implements Initializable {

    @FXML  private Label projectName;

    @FXML  private Label projectManager;

    @FXML   private Button acceptBtn;

    @FXML   private Button declineBtn;
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
            System.out.println("project request");
        
    }

}
