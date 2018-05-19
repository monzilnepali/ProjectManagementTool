package com.info.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.info.dao.ProjectDao;
import com.info.model.Project;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ProjectCreationController extends HomeController  implements Initializable{

  

    @FXML private  JFXTextField projectTitle;
    @FXML  private JFXTextField projectCategories;
    @FXML  private TextArea projectDescription;
    @FXML  private  TextArea projectTeamMember;
	
	@FXML private Pane projectCreation_projectDetail;
    @FXML private Button projectCreation_NextBtn;
    @FXML  private Pane projectCreation_projectTeam;
	@FXML private StackPane pane;
	@FXML private Button AddTeamMemberBtn;
	@FXML  private FontAwesomeIconView AddMoreTeamMemberBtn;
	@FXML  private AnchorPane addTeamMemberPane;
	@FXML private Button projectCreationBackBtn;
	@FXML private Button projectCreationFinishBtn;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		System.out.println("\n\nproject Creation called");
		projectCreation_projectDetail.setVisible(true);
		projectCreation_projectTeam.setVisible(false);
		projectCreation_NextBtn.setOnAction(e->{
			
			projectCreation_projectDetail.setVisible(false);
			projectCreation_projectTeam.setVisible(true);
			
		});
		projectCreationBackBtn.setOnAction(e->{
			//showing projectdetail pane
			projectCreation_projectDetail.setVisible(true);
			projectCreation_projectTeam.setVisible(false);
		});
		projectCreationFinishBtn.setOnAction(e->{
			//getting data from form and insert into database
			
			
			Project pro=new Project();
			
			pro.setprojectTitle(projectTitle.getText());
			pro.setProjectDesc(projectDescription.getText());
			pro.setCategories(projectCategories.getText());
			List<String> team=new ArrayList<String>();
			//getting email from team Members field
			//separating them using string tokenizer 
			System.out.println("project team member"+projectTeamMember.getText());
			for(String email:projectTeamMember.getText().split(",")) {
				System.out.println("email "+email);
				team.add(email);
			}
			
			
			pro.setTeamMember(team);
			CurrentUserSingleton tmp=CurrentUserSingleton.getInstance();	
			ExecutorService executor=Executors.newFixedThreadPool(3);
			Callable<Boolean> c = ()->{
				System.out.println(Thread.currentThread().getName());
				 return ProjectDao.CreatProject(pro,tmp.getVuser().getUser_id());
				 
			};
			
			Future<Boolean> future =executor.submit(c);
			
			try {
				Boolean result =future.get();
				if(result) {
					Stage currentStage=(Stage)((Node)e.getSource()).getScene().getWindow();
					
					currentStage.close();
					HomeController.getProjectDetail();
				}
			} catch (Exception e1) {
				
				e1.printStackTrace();
			} 
			
		});
		
	}

}
