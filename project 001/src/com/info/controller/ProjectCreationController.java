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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

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
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import secondPhaseController.HomeNewController;

public class ProjectCreationController  implements Initializable {

    @FXML
    private TextField projectTitle;
    @FXML
    private TextField projectCategories;
    @FXML
    private TextArea projectDescription;
    @FXML
    private TextField projectTeamMember;
    @FXML
    private Pane projectCreation_projectDetail;
    @FXML
    private Button projectCreation_NextBtn;
    @FXML
    private Pane projectCreation_projectTeam;
    @FXML
    private StackPane pane;
  
    @FXML
    private Button projectCreationFinishBtn;
    @FXML
    private FontAwesomeIconView  uploadDocs;
    @FXML
    private ListView<String> docsList;
    @FXML
    private JFXProgressBar taskProgress;
    @FXML
    private Label waitLabel;
    @FXML private Label uploadLabel;
    
    @FXML private Button profileUploadBtn;
    @FXML private Label projectProfileImage;
    @FXML   private JFXProgressBar progressBar;
    private File profileImage;
    
    ObservableList<String> list = FXCollections.observableArrayList();
    private List<File> files;
    private HomeNewController controller;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
      //  taskProgress.setVisible(false);
       // waitLabel.setVisible(false);
        projectCreationFinishBtn.setVisible(false);
        System.out.println("\n\nproject Creation called");
        CurrentUserSingleton tmp = CurrentUserSingleton.getInstance();
        docsList.setVisible(false);
        uploadLabel.setVisible(true);
        uploadDocs.setVisible(true);
        progressBar.setVisible(false);
        projectProfileImage.setVisible(true);//profile Image name
        // filechooser
        FileChooser fc = new FileChooser();
        // restrict the file selection
        // allowing picture extenstion and office extension

        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Picture", "*.jpg", "*.png"),
                new FileChooser.ExtensionFilter("Word Document", "*.docx "),
                new FileChooser.ExtensionFilter("Excel", "*.xlsx"),
                new FileChooser.ExtensionFilter("powerpoint", "*.ppt"),
                new FileChooser.ExtensionFilter("Text File", "*.txt")

        );
        
        
        //profile image file chooser
        FileChooser imageChooser = new FileChooser();
        // restrict the file selection
        // allowing picture extenstion and office extension

        imageChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Picture", "*.jpg", "*.png")
                

        );
        
        
        projectCreation_projectDetail.setVisible(true);
        projectCreation_projectTeam.setVisible(false);
        projectCreation_NextBtn.setOnAction(e -> {

            projectCreation_projectDetail.setVisible(false);
            projectCreation_projectTeam.setVisible(true);

        });
        uploadDocs.setOnMouseClicked(e -> {

            list.clear();
            // uploading file in websever
            Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            files = fc.showOpenMultipleDialog(currentStage);
            // getting selected file directory
            if (files != null) {
                for (File f : files) {
                    if (f != null) {
                        System.out.println("path" + f.getAbsolutePath());
                        list.add(f.getName() + "\t" + fileSize(f.length()));

                    }
                }
                uploadLabel.setVisible(false);
                uploadDocs.setVisible(false);
                docsList.setVisible(true);
                projectCreationFinishBtn.setVisible(true);
                docsList.setItems(list);

            } else {
                System.out.println("nothing to upload ");
            }
       
        });
        
        //profile image upload
        
        profileUploadBtn.setOnAction(e->{
            
            Stage currentStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            profileImage=imageChooser.showOpenDialog(currentStage);
            
            projectProfileImage.setVisible(true);
            projectProfileImage.setText(profileImage.getName()+"\t"+fileSize(profileImage.length()));
            
            
            
        });
       
        projectCreationFinishBtn.setOnAction(e -> {
            
            progressBar.setVisible(true);
            
            File src=profileImage;
            String fileExtension=FilenameUtils.getExtension(src.getName());
            File descDir=new File("D:\\client\\"+projectTitle.getText());
            descDir.mkdir();
            File temp=new File(descDir,projectTitle.getText()+"."+fileExtension);
           
            try {
                temp.createNewFile();
                FileUtils.copyFile(src, temp);
                System.out.println("file copied");
            } catch (IOException e2) {
                // TODO Auto-generated catch block
                e2.printStackTrace();
            }
            
            //writing that project image to client side direct;y
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.submit(() -> {
                String threadName = Thread.currentThread().getName();
               //copy file from user directory to project dir
            });

            // getting data from form and insert into database
          //  taskProgress.setVisible(true);
          //  waitLabel.setVisible(true);
            System.out.println("project creation finished button called");

            Project pro = new Project();

            pro.setprojectTitle(projectTitle.getText());
            pro.setProjectDesc(projectDescription.getText());
            pro.setCategories(projectCategories.getText());
            List<String> teamMemberList = new ArrayList<String>();
            // getting email from team Members field
            // separating them using string tokenizer
            // System.out.println("project team
            // member"+projectTeamMember.getText());
            for (String email : projectTeamMember.getText().split(",")) {
                // System.out.println("email "+email);
                teamMemberList.add(email);
            }

            pro.setTeamMember(teamMemberList);
            pro.setFileList(files);
            //setting project image
            pro.setProjectImageFile(profileImage);
            // sending request to server for creation of project
            tmp.getOut().println("projectCreation");

            // sending the manager id to server
            tmp.getOut().println(tmp.getVuser().getUser_id());

            try {
                // writing the object to the server
                tmp.getObjOut().writeObject(pro);
                
                //waiting for server response
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        });

    }

    public static String fileSize(double size) {
        // converting bytes to KB and MB for displaying filesize of document
        // uploaded

        if (size <= 1024 * 1024) {
            return Math.round(size / 1024) + "KB";
        } else {
            return Math.round(size / (1024 * 1024)) + "MB";
        }

    }

    public void setData(HomeNewController homeNewController) {
       this.controller=homeNewController;
        
    }

}
