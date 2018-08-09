package com.info.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.info.dao.ProjectDao;
import com.info.model.TaskModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ProjectTaskCompletedController implements Initializable {

    

    @FXML   private TableView<TaskModel> taskTable;
    @FXML   private TableView<TaskModel> taskTableComplete;
    @FXML private TableColumn<TaskModel,String> taskName; 
    @FXML private TableColumn<TaskModel,String> userName;
    @FXML private TableColumn<TaskModel,String> taskDeadLine;
    @FXML private TableColumn<TaskModel,String> taskStatus;
    @FXML private TableColumn<TaskModel,String> taskPriority;
    
    CurrentUserSingleton tmp=CurrentUserSingleton.getInstance();
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
      System.out.println("task completed Controller called");
        loadData(); 
        
        
        
    }
    public void loadData() {
        //populating table
        System.out.println("load data called");
                
   
         ObservableList<TaskModel> taskList=FXCollections.observableArrayList(ProjectDao.getCompletedTaskList(tmp.getActiveProjectId(),tmp.getCurrentUserRoleInActiveProject(),tmp.getVuser().getUser_id()));
         for(TaskModel ts:taskList) {
             System.out.println("task name"+ts.getTaskName());
         }
         
         taskName.setCellValueFactory(new PropertyValueFactory<TaskModel,String>("taskName"));
         userName.setCellValueFactory(new PropertyValueFactory<TaskModel,String>("taskAssignToName"));
         taskDeadLine.setCellValueFactory(new PropertyValueFactory<TaskModel,String>("taskDeadLine"));
         taskStatus.setCellValueFactory(new PropertyValueFactory<TaskModel,String>("taskStatus"));
         taskPriority.setCellValueFactory(new PropertyValueFactory<TaskModel,String>("taskPriority"));
         
         taskTable.setItems(taskList);

    }

}
