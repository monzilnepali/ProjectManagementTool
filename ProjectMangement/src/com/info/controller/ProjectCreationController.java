package com.info.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
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
	ObservableList<String>list=FXCollections.observableArrayList();
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
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
			List<File> files = fc.showOpenMultipleDialog(currentStage);
			//getting selected fie directory
			for(File f:files) {
				if(f!=null) {
					System.out.println("path"+f.getAbsolutePath());
					list.add(f.getName()+"\t"+fileSize(f.length()));
					
				}
			}
			docsList.setVisible(true);
			docsList.setItems(list);
			System.out.println("file chooser finised");
			try {
				Socket socket=new Socket("localhost",9090);
				//sending data to server
				PrintWriter out=new PrintWriter(socket.getOutputStream(),true);
				out.println("upload");
				//getting response for the server 
				BufferedReader input=new BufferedReader(new InputStreamReader(socket.getInputStream()));
				System.out.println(input.readLine());
				//sending file in server
				
				BufferedOutputStream bufferout=new BufferedOutputStream(socket.getOutputStream());
				BufferedInputStream fileReader;
				for(File f:files) {
					if(f!=null) {
						System.out.println("upload file in server");
						//sending file name
					    out.println(projectTitle.getText());
					    PrintWriter out1=new PrintWriter(socket.getOutputStream(),true);
						out1.println(f.getName());
						fileReader=new BufferedInputStream(new FileInputStream(f));
						byte[] buffer=new byte[1024];
						int bytesRead=0;
						while((bytesRead=fileReader.read(buffer))!=-1) {
							bufferout.write(buffer, 0, bytesRead);
							bufferout.flush();
							
						}
						
						
					}
				}
			         socket.close();
				
				
			} catch (UnknownHostException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
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
	
	public String fileSize(double size) {
		//converting bytes to KB and MB for displaying filesize of document uploaded
		
		if(size<=1024*1024) {
			return Math.round(size/1024)+"KB";
		}else {
			return Math.round(size/(1024*1024))+"MB";
		}
		
		
	}

}
