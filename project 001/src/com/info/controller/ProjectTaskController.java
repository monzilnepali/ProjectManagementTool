package com.info.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.info.dao.ProjectDao;
import com.info.model.TaskModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProjectTaskController implements Initializable {

	
	 @FXML   private TableView<TaskModel> taskTable;
	 @FXML   private TableView<TaskModel> taskTableComplete;
	 @FXML private TableColumn<TaskModel,String> taskName; 
	 @FXML private TableColumn<TaskModel,String> userName;
	 @FXML private TableColumn<TaskModel,String> taskDeadLine;
	 @FXML private TableColumn<TaskModel,String> taskStatus;
	 @FXML private TableColumn<TaskModel,String> taskPriority;
	 
	 
	 private static String role;
	 
	 
	 @FXML	 private Button AddTaskBtn;
	
	
	 CurrentUserSingleton tmp=CurrentUserSingleton.getInstance();
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		System.out.println("project Task controller calleld");
		AddTaskBtn.setVisible(false);
		loadData();
		
		AddTaskBtn.setOnAction(e->{
			System.out.println("add task button clicked");
			//opening addtask scene 
			FXMLLoader loader=new FXMLLoader();
			loader.setLocation(getClass().getResource("/application/TaskAdd.fxml"));
			try {
				loader.load();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			//TaskAddController controller=loader.getController();
			//controller.setData(currentProjectId,this);
			Parent p=loader.getRoot();
			Scene taskAddScene=new Scene(p);
			Stage taskAddStage=new Stage();
			Stage previousStage=(Stage)((Node)e.getSource()).getScene().getWindow();
			taskAddStage.setScene(taskAddScene);
			taskAddStage.setTitle("Add Task");
			taskAddStage.initModality(Modality.WINDOW_MODAL);
			taskAddStage.initOwner(previousStage);
			taskAddStage.setResizable(false);
			taskAddStage.show();
			
			
		
			
			
		});
		
		
		//on selecting the row of table
		taskTable.setOnMouseClicked(e->{
			TaskModel selectedTask=taskTable.getSelectionModel().getSelectedItem();
			System.out.println("the selected row is "+selectedTask.getTaskName());
		});
		
		

		if(tmp.getCurrentUserRoleInActiveProject()==1) {
			AddTaskBtn.setVisible(true);
		}

		taskTable.setOnMouseClicked(e->{
			if(e.getClickCount()==2) {
				if(tmp.getCurrentUserRoleInActiveProject()==1) {
				
					
					System.out.println("open");
					TaskModel selectedTask=taskTable.getSelectionModel().getSelectedItem();
					FXMLLoader loader=new FXMLLoader();
					loader.setLocation(getClass().getResource("/application/TaskCompletion.fxml"));
					try {
						loader.load();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					TaskCompletionController controller=loader.getController();
					controller.setData(selectedTask,this);
					Parent p=loader.getRoot();
					Scene scene=new Scene(p);
					Stage newStage=new Stage();
					newStage.setScene(scene);
					Stage previousStage=(Stage)((Node)e.getSource()).getScene().getWindow();
					newStage.initModality(Modality.WINDOW_MODAL);
					newStage.initOwner(previousStage);
					newStage.setTitle("update task status");
					newStage.show();
					
				}else {
					System.out.println("open");
					TaskModel selectedTask=taskTable.getSelectionModel().getSelectedItem();
					FXMLLoader loader=new FXMLLoader();
					loader.setLocation(getClass().getResource("/application/TaskStatusUpdate.fxml"));
					try {
						loader.load();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
					TaskStatusUpdateController controller=loader.getController();
					controller.setData(selectedTask,this);
					Parent p=loader.getRoot();
					Scene scene=new Scene(p);
					Stage newStage=new Stage();
					newStage.setScene(scene);
					Stage previousStage=(Stage)((Node)e.getSource()).getScene().getWindow();
					newStage.initModality(Modality.WINDOW_MODAL);
					newStage.initOwner(previousStage);
					newStage.setTitle("update task status");
					newStage.show();
					
					
					
				}
			}
		});
	
	}
	public void loadData() {
		//populating table
		System.out.println("load data called");
				
		
		
	
		
		
		
				
		 ObservableList<TaskModel> taskList=FXCollections.observableArrayList(ProjectDao.getTaskList(tmp.getActiveProjectId(),tmp.getCurrentUserRoleInActiveProject(),tmp.getVuser().getUser_id()));
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
