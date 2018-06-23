package com.info.controller;

import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.info.dao.ProjectDao;
import com.info.model.Task;
import com.info.model.User;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class TaskAddController implements Initializable {

	 @FXML  private TextField taskNameField;
     @FXML  private DatePicker taskDeadline;
	@FXML private ComboBox<User> teamMemberList;
	@FXML private ComboBox<String> taskPriority;
	@FXML private Button createTasktBtn;
	private static int currentProjectId;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
			System.out.println("task add controller callled");
		
			
		
	}
	
	public void setData(int currentProjectId,ProjectTaskController controller) {
		System.out.println("currentProjectId"+currentProjectId);
		TaskAddController.currentProjectId=currentProjectId;
		List<String> list=new ArrayList<String>();
		list.add("Emergency");
		list.add("High");
		list.add("Normal");
		list.add("Low");
		ObservableList<String> olist=FXCollections.observableArrayList(list);
		taskPriority.setItems(olist);
		//adding project Team member in list for dropmenu box
		ObservableList<User> teamlist=FXCollections.observableArrayList(ProjectDao.getTeamMemberNameOfProject(currentProjectId));
		teamMemberList.setItems(teamlist);
		
		
		createTasktBtn.setOnAction(e->{
			//onclicking create button
			
			//checking the input field
			//getting date as string from datepicker 
			SimpleDateFormat ft =new SimpleDateFormat ("E yyyy-MM-dd");
			LocalDate  date=taskDeadline.getValue();
			Date date1=java.sql.Date.valueOf(date);
			String deadlinedate=ft.format(date1);
			
			if(taskNameField.getText().equals("") && deadlinedate.equals("") && teamMemberList.getValue().equals("") && taskPriority.getValue().equals("")  ) {
                //show error to check inputs				
				System.out.println("please check inputs");
			}else {
				//ready to insert task data in database
				Task newTask=new Task();
				newTask.setTaskName(taskNameField.getText());
				newTask.setTaskDeadLine(deadlinedate);
				newTask.setTaskAssignTo(teamMemberList.getValue().getUser_id());
				newTask.setTaskPriority(taskPriority.getValue());
				newTask.setProjectId(currentProjectId);
				
				if(ProjectDao.addNewTask(newTask)) {
					//close the window
					Stage currentStage=(Stage)((Node)e.getSource()).getScene().getWindow();
					
					
					
					controller.loadData();
					currentStage.close();
					
				}else {
					//show error message
					System.out.println("error while adding new task in database");
				}
			}
			
			
			
		});
		
		
	}
	

}
