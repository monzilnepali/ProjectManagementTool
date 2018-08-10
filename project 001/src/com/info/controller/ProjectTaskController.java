package com.info.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.info.dao.ProjectDao;
import com.info.model.TaskModel;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
	 @FXML private Button refreshBtn;
	
	 ObservableList<TaskModel> taskList;
	 CurrentUserSingleton tmp=CurrentUserSingleton.getInstance();
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	
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
			TaskAddController controller=loader.getController();
			controller.setData(this);
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
		refreshBtn.setOnAction(e->{
		    System.out.println("table refresh");
		    loadData();
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
				//marking task as completed
					
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
				    //if task status is not running allowing team member to view task info and marking as running state
				    
					System.out.println("open");
					TaskModel selectedTask=taskTable.getSelectionModel().getSelectedItem();
					FXMLLoader loader=new FXMLLoader();
					
					  if(tmp.getActiveTab().equals("#  Not Running")) {
					      //allowing team member to view task info and marking task as in running state
					     
					      loader.setLocation(getClass().getResource("/application/TaskInitialStatusUpdate.fxml"));
		                    try {
		                        loader.load();
		                    } catch (IOException e1) {
		                        e1.printStackTrace();
		                    }
		                    TaskInitialStatusUpdateController controller=loader.getController();
		                    controller.setData(selectedTask,this);
		                    
					      
					  }
					  if(tmp.getActiveTab().equals("#  Running")) {
                          //allowing team member to view task info and marking task as in running state
                         
                          loader.setLocation(getClass().getResource("/application/TaskRunningStatusUpdate.fxml"));
                            try {
                                loader.load();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            TaskRunningStatusUpdateController controller=loader.getController();
                            controller.setData(selectedTask,this);
                            
                          
                      }
					
//					loader.setLocation(getClass().getResource("/application/TaskStatusUpdate.fxml"));
//					try {
//						loader.load();
//					} catch (IOException e1) {
//						e1.printStackTrace();
//					}
//					TaskStatusUpdateController controller=loader.getController();
//					controller.setData(selectedTask,this);
//					
					
					
					
					
					
					
					
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
		
		taskList.addListener(new ListChangeListener<TaskModel>() {

            @Override
            public void onChanged(Change<? extends TaskModel> arg0) {
               System.out.println("list changed");
               loadData();
                
            }
		    
		});
	
	}
	public void loadData() {
		//populating table
		
				int task_status = 0;
				System.out.println("the active tab in load data is"+tmp.getActiveTab());
              if(tmp.getActiveTab().equals("#  Not Running")) {
                  System.out.println("showing not running task");
                  task_status=0;
              }else if(tmp.getActiveTab().equals("#  Running")) {
                  System.out.println("showing  running task");
                  task_status=1;
              }else if(tmp.getActiveTab().equals("#  Review Phase")) {
                  task_status=2;
              }else if(tmp.getActiveTab().equals("#  Completed")) {
                  task_status=3;
              }
				
		  taskList=FXCollections.observableArrayList(ProjectDao.getTaskList(tmp.getActiveProjectId(),tmp.getCurrentUserRoleInActiveProject(),tmp.getVuser().getUser_id(),task_status));
        
         taskName.setCellValueFactory(new PropertyValueFactory<TaskModel,String>("taskName"));
         userName.setCellValueFactory(new PropertyValueFactory<TaskModel,String>("taskAssignToName"));
         taskDeadLine.setCellValueFactory(new PropertyValueFactory<TaskModel,String>("taskDeadLine"));
         taskStatus.setCellValueFactory(new PropertyValueFactory<TaskModel,String>("taskStatus"));
         taskPriority.setCellValueFactory(new PropertyValueFactory<TaskModel,String>("taskPriority"));
         
         taskTable.setItems(taskList);

	}
    public void update(TaskModel newTask) {
      
        taskList.add(newTask);
    }
    public void removeTask(int taskId) {
        //removing task object from obserablelist
        System.out.println("remove task id is"+taskId);
        TaskModel removeObject = null;
       for(TaskModel tm:taskList) {
           if(tm.getTaskId()==taskId) {
               removeObject=tm;
              break;
           }
       }
       if(removeObject!=null) {
           taskList.remove(removeObject);
       }
    }
    
  

}
