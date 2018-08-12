package com.info.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.info.dao.ProjectDao;
import com.info.model.User;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ProjectTeamController implements Initializable {
    @FXML
    private Label projectManageName;

    @FXML
    private VBox teamMember;
    static CurrentUserSingleton tmp=CurrentUserSingleton.getInstance();
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        
        System.out.println("project team controller called");
        String projectmanager=ProjectDao.getProjectManagerViaProjectId(tmp.getActiveProjectId());
        projectManageName.setText(projectmanager);
        
        List<User> teamMemberList=ProjectDao.getTeamMemberNameOfProject(tmp.getActiveProjectId());
        //spacing of vbox
        teamMember.setSpacing(10);
       for(User user:teamMemberList) {
           Label label=new Label();
           label.setStyle("-fx-text-fill:white;-fx-font-size:18px;-fx-background-color:#2F3136");
           label.setText("Name: "+user.getUser_name());
           teamMember.getChildren().add(label);
       }
        

        //getting manager of current project
        
    }

}
