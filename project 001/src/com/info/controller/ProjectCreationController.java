package com.info.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import com.info.dao.ProjectDao;
import com.info.model.Project;
import com.jfoenix.controls.*;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
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
	@FXML private Button uploadDocs;
	@FXML private ListView<String> docsList;
	@FXML private JFXProgressBar taskProgress;
	ObservableList<String>list=FXCollections.observableArrayList();
    private List<File> files;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		taskProgress.setVisible(false);
		
		
	
		System.out.println("\n\nproject Creation called");
		CurrentUserSingleton tmp=CurrentUserSingleton.getInstance();	
		docsList.setVisible(false);
		//filechooser
		 FileChooser fc=new FileChooser();
		//restrict the file selection
		//allowing picture extenstion and office extension
		

				fc.getExtensionFilters().addAll(
						new FileChooser.ExtensionFilter("Picture", "*.jpg","*.png"),
						new FileChooser.ExtensionFilter("Word Document", "*.docx "),
						new FileChooser.ExtensionFilter("Excel", "*.xlsx"),
						new FileChooser.ExtensionFilter("powerpoint", "*.ppt"),
						new FileChooser.ExtensionFilter("Text File", "*.txt")
						
						);
		projectCreation_projectDetail.setVisible(true);
		projectCreation_projectTeam.setVisible(false);
		projectCreation_NextBtn.setOnAction(e->{
			
			projectCreation_projectDetail.setVisible(false);
			projectCreation_projectTeam.setVisible(true);
			
		});
		uploadDocs.setOnAction(e->{

			list.clear();
			//uploading file in websever
			Stage currentStage=(Stage)((Node)e.getSource()).getScene().getWindow();
		    files = fc.showOpenMultipleDialog(currentStage);
			//getting selected file directory
		    if(files!=null) {
			for(File f:files) {
				if(f!=null) {
					System.out.println("path"+f.getAbsolutePath());
					list.add(f.getName()+"\t"+fileSize(f.length()));
					
					
				}
			}
			docsList.setVisible(true);
			docsList.setItems(list);
			
		    
			
			
			
			
			
		    }else {
		    	System.out.println("nothing to upload ");
		    }
		});
		projectCreationBackBtn.setOnAction(e->{
			//showing projectdetail pane
			projectCreation_projectDetail.setVisible(true);
			projectCreation_projectTeam.setVisible(false);
		});
		projectCreationFinishBtn.setOnAction(e->{
			//getting data from form and insert into database
			
		
			System.out.println("project creation finished button called");
		
			Project pro=new Project();
			
			pro.setprojectTitle(projectTitle.getText());
			pro.setProjectDesc(projectDescription.getText());
			pro.setCategories(projectCategories.getText());
			List<String> teamMemberList=new ArrayList<String>();
			//getting email from team Members field
			//separating them using string tokenizer 
		//	System.out.println("project team member"+projectTeamMember.getText());
			for(String email:projectTeamMember.getText().split(",")) {
			//	System.out.println("email "+email);
				teamMemberList.add(email);
			}
			
			
			pro.setTeamMember(teamMemberList);
			pro.setFileList(files);
			//writing the object to the server
			tmp.getOut().println("projectCreation");
			
			//sending the manager id to server
			
			tmp.getOut().println(tmp.getVuser().getUser_id());
			
			try {
				tmp.getObjOut().writeObject(pro);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
			
			
			
			
			
		});
		
	}
	
	public static String fileSize(double size) {
		//converting bytes to KB and MB for displaying filesize of document uploaded
		
		if(size<=1024*1024) {
			return Math.round(size/1024)+"KB";
		}else {
			return Math.round(size/(1024*1024))+"MB";
		}
		
		
	}

}
