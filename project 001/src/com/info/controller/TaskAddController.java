package com.info.controller;

import java.io.IOException;
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
	static CurrentUserSingleton tmp=CurrentUserSingleton.getInstance();
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
				//sending newtask data to server to store data in database
				
			   tmp.getOut().println("TaskAdd");	
			   try {
				   System.out.println("writing objec to server");
				tmp.getObjOut().writeObject(newTask);//transfer object to server
				
				System.out.println("response back from server");
				
					Stage currentStage=(Stage)((Node)e.getSource()).getScene().getWindow();
					
					
					
					controller.loadData();
					currentStage.close();
				
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
				
				
				
				
				
			}
			
			
			
		});
		
		
	}
	

}
