package com.info.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Date;
import com.info.dao.ProjectDao;
import com.info.dao.UserDao;
import com.info.model.TaskModel;
import com.info.model.User;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class TaskAddController implements Initializable {
@FXML private Pane firstPane;
@FXML private Pane secondPane;
	 @FXML  private TextField taskNameField;
     @FXML  private DatePicker taskDeadline;
	@FXML private ComboBox<User> teamMemberList;
	@FXML private ComboBox<String> taskPriority;
	@FXML private Button createTasktBtn;
	@FXML
    private FontAwesomeIconView uploadDocsButton;
	@FXML private Button nextBtn;

    @FXML private ListView<String> docsList;
    @FXML private Label docsUploadLabel;
    @FXML private  Button paneBack;
    ObservableList<String>listdocs=FXCollections.observableArrayList();
    private List<File> files;
    private FileChooser fc;
	
	static CurrentUserSingleton tmp=CurrentUserSingleton.getInstance();
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
			System.out.println("task add controller callled");
			firstPane.setVisible(true);
			secondPane.setVisible(false);
			docsList.setVisible(false);
			createTasktBtn.setVisible(false);
			docsUploadLabel.setVisible(false);
			
			//setting task deadline datepicker min. date to present date
			
			LocalDate localDate = LocalDate.now();//For reference
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
			String formattedString = localDate.format(formatter);
		
			//filechooser
			  fc=new FileChooser();
			//restrict the file selection
			//allowing picture extenstion and office extension
			

					fc.getExtensionFilters().addAll(
							new FileChooser.ExtensionFilter("Picture", "*.jpg","*.png"),
							new FileChooser.ExtensionFilter("Word Document", "*.docx "),
							new FileChooser.ExtensionFilter("Excel", "*.xlsx"),
							new FileChooser.ExtensionFilter("powerpoint", "*.ppt"),
							new FileChooser.ExtensionFilter("Text File", "*.txt")
							
							);
		
	
	

	
		List<String> list=new ArrayList<String>();
		list.add("Emergency");
		list.add("High");
		list.add("Normal");
		list.add("Low");
		ObservableList<String> olist=FXCollections.observableArrayList(list);
		taskPriority.setItems(olist);
		//adding project Team member in list for dropmenu box
		ObservableList<User> teamlist=FXCollections.observableArrayList(ProjectDao.getTeamMemberNameOfProject(tmp.getActiveProjectId()));
		teamMemberList.setItems(teamlist);
		paneBack.setOnAction(e->{
		    firstPane.setVisible(true);
            secondPane.setVisible(false);
		});
		nextBtn.setOnAction(e->{
		    //switching the pane
		    firstPane.setVisible(false);
            secondPane.setVisible(true);
		    
		});
		uploadDocsButton.setOnMouseClicked(e->{
		    System.out.println("upload btn clicked");
		    System.out.println("upoad btn clicked");
            //onclicking the upload docs btn
            list.clear();
            //uploading file in websever
            Stage currentStage=(Stage)((Node)e.getSource()).getScene().getWindow();
            files = fc.showOpenMultipleDialog(currentStage);
            //getting selected file directory
            if(files!=null) {
            for(File f:files) {
                if(f!=null) {
                    System.out.println("path"+f.getAbsolutePath());
                    listdocs.add(f.getName()+"\t"+ ProjectCreationController.fileSize(f.length()) );
                    
                    
                }
            }
            docsUploadLabel.setVisible(true);
            docsList.setVisible(true);
            createTasktBtn.setVisible(true);
            docsList.setItems(listdocs);
            
            }   
		});
		
		
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
				TaskModel newTask=new TaskModel();
				newTask.setTaskName(taskNameField.getText());
				newTask.setTaskDeadLine(deadlinedate);
				newTask.setTaskAssignTo(teamMemberList.getValue().getUser_id());
				newTask.setTaskPriority(taskPriority.getValue());
				newTask.setProjectId(tmp.getActiveProjectId());
				newTask.setTaskCreationDate (UserDao.formattedDate(new Date()));
				
				newTask.setFile(files);
				
				
				//sending newtask data to server to store data in database
				
			   tmp.getOut().println("TaskAdd");	
			   try {
				   System.out.println("writing objec to server");
				   tmp.getObjOut().writeObject(newTask);//transfer object to server
				
				  System.out.println("response back from server");
				
					Stage currentStage=(Stage)((Node)e.getSource()).getScene().getWindow();
					
					
					
					//controller.loadData();
					currentStage.close();
				
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
				
				
				
				
				
			}
			
			
			
		});
		
		
	
	
	}
}
