package com.info.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.info.dao.ProjectDao;
import com.info.model.ProjectFile;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProjectFileController implements Initializable {

    @FXML private TableView<ProjectFile> fileTable;

    @FXML private TableColumn<ProjectFile, String> Title;

    @FXML private TableColumn<ProjectFile, String> Member;

    @FXML private TableColumn<ProjectFile, String> ModifiedDate;

    CurrentUserSingleton tmp=CurrentUserSingleton.getInstance();

    
    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
       System.out.println("the project file controller called");
       
       //getting data about completed task in database
       ObservableList<ProjectFile> olist=FXCollections.observableArrayList( ProjectDao.getProjectCompletedFile(tmp.getActiveProjectId()));
       Title.setCellValueFactory(new PropertyValueFactory<ProjectFile,String>("fileName"));
       Member.setCellValueFactory(new PropertyValueFactory<ProjectFile,String>("TeamMemberName"));
       ModifiedDate.setCellValueFactory(new PropertyValueFactory<ProjectFile,String>("lastModified"));
       fileTable.setItems(olist);
       
       
       
       fileTable.setOnMouseClicked(e->{
           if(e.getClickCount()==2) {
               ProjectFile pf=fileTable.getSelectionModel().getSelectedItem();
               //showing window which allow other team member to download and provide feature to comment in that project
               FXMLLoader loader =new FXMLLoader();
             loader.setLocation(getClass().getResource("/application/ProjectFileDetails.fxml"));
             try {
                loader.load();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
             ProjectFileDetailsController controller=new ProjectFileDetailsController();
             controller.setdata(pf);
             Parent p=loader.getRoot();
             Scene newScene=new Scene(p);
             Stage stage=new Stage();
             Stage previousStage=(Stage)((Node)e.getSource()).getScene().getWindow();
             stage.setTitle("project file details");
             stage.initModality(Modality.WINDOW_MODAL);
             stage.initOwner(previousStage);
             stage.show();
             

             
             
               
               
               
           }
       
       });
    }

}
