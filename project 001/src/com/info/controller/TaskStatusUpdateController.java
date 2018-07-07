package com.info.controller;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.info.dao.ProjectDao;
import com.info.model.Task;
import com.jfoenix.controls.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class TaskStatusUpdateController implements Initializable {

	    
	    @FXML
	    private TextField taskTitle;

	    @FXML
	    private TextField taskDesc;

	    @FXML
	    private Button uploadFileBtn;

	    @FXML
	    private ListView<String> fileList;
	    
	    @FXML
	    private CheckBox checkBox1;

	    @FXML
	    private CheckBox checkBox2;
	    
	    private List<File> files;
	    ObservableList<String> list=FXCollections.observableArrayList();
	    
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		
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
	
	
				uploadFileBtn.setOnAction(e->{
					//opening file chooser
					Stage currentStage=(Stage)((Node)e.getSource()).getScene().getWindow();
				    files = fc.showOpenMultipleDialog(currentStage);
					//getting selected file directory
				    if(files!=null) {
					for(File f:files) {
						if(f!=null) {
							System.out.println("path"+f.getAbsolutePath());
							list.add(f.getName()+"\t"+ProjectCreationController.fileSize(f.length()));
							
							
						}
					}
					
					fileList.setItems(list);
					
				    }
				});
				checkBox1.setOnMouseClicked(e->{
					
					checkBox1.setSelected(true);
					checkBox2.setSelected(false);
				});	
				
				checkBox2.setOnMouseClicked(e->{
					
					checkBox2.setSelected(true);
					checkBox1.setSelected(false);
				});	
				
				
		
		
	}
	public void setData(Task task,ProjectTaskController controller) {
		System.out.println("set data task status update called");
		//filling data in form
		taskTitle.setDisable(true);
		taskTitle.setText(task.getTaskName());
		
		 
	
	}

}
