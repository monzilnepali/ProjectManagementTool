package com.info.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.info.dao.ProjectDao;
import com.info.model.Task;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

	
	 @FXML   private TableView<Task> taskTable;
	 @FXML private TableColumn<Task,String> taskName; 
	 @FXML private TableColumn<Task,String> userName;
	 @FXML private TableColumn<Task,String> taskDeadLine;
	 @FXML private TableColumn<Task,String> taskStatus;
	 @FXML private TableColumn<Task,String> taskPriority;
	 
	 
	 private static String role;
	 
	 
	 @FXML	 private Button AddTaskBtn;
	 private static int currentProjectId;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		System.out.println("project Task controller calleld");
		AddTaskBtn.setVisible(false);
		
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
			TaskAddController controller=loader.getController();
			controller.setData(currentProjectId,this);
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
			Task selectedTask=taskTable.getSelectionModel().getSelectedItem();
			System.out.println("the selected row is "+selectedTask.getTaskName());
		});
		
	}
	public void setData(Integer integer,String role) {
		
		currentProjectId=Integer.valueOf(integer);
		ProjectTaskController.role=role;
		if(role.equals(" Manager")) {
			AddTaskBtn.setVisible(true);
		}
		loadData();
		
		taskTable.setOnMouseClicked(e->{
			if(e.getClickCount()==2) {
				if(ProjectTaskController.role.equals(" Manager")) {
					System.out.println("no open");
					//opening window to allow manager to update the task
				}else {
					System.out.println("open");
					Task selectedTask=taskTable.getSelectionModel().getSelectedItem();
					FXMLLoader loader=new FXMLLoader();
					loader.setLocation(getClass().getResource("/application/TaskStatusUpdate.fxml"));
					try {
						loader.load();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
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
				CurrentUserSingleton tmp=CurrentUserSingleton.getInstance();
				
				ObservableList<Task> taskList=FXCollections.observableArrayList(ProjectDao.getTaskList(currentProjectId,ProjectTaskController.role,tmp.getVuser().getUser_id()));
				for(Task ts:taskList) {
					System.out.println("task name"+ts.getTaskName());
				}
				
				taskName.setCellValueFactory(new PropertyValueFactory<Task,String>("taskName"));
				userName.setCellValueFactory(new PropertyValueFactory<Task,String>("taskAssignToName"));
				taskDeadLine.setCellValueFactory(new PropertyValueFactory<Task,String>("taskDeadLine"));
				taskStatus.setCellValueFactory(new PropertyValueFactory<Task,String>("taskStatus"));
				taskPriority.setCellValueFactory(new PropertyValueFactory<Task,String>("taskPriority"));
				
				taskTable.setItems(taskList);
		
		
	}

}
