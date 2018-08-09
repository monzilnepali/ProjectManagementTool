package com.info.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.info.dao.ProjectDao;
import com.info.model.TaskModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class TaskInitialStatusUpdateController implements Initializable {
    @FXML
    private Label projectName;

    @FXML
    private Label taskTitle;

    @FXML
    private TextArea  taskDesc;

    @FXML
    private Button docsDownloadBtn;

    @FXML
    private ListView<String> docsList;

    @FXML
    private Label taskAssignDate;
    @FXML
    private Label taskDayLeft;

    @FXML
    private Label taskDeadlineDate;
    @FXML
    private CheckBox taskRunningBtn;

 
    @FXML
    private Button taskUpdateBtn;
   

    private List<String> list = new ArrayList<String>();
    ObservableList<String> olist;

    static CurrentUserSingleton tmp = CurrentUserSingleton.getInstance(); // current
                                                                            // user
                                                                            // object
    
    //for task file upload
     private FileChooser fc;
     private List<File> fileList;//adding file from file chooser
     ObservableList<String>Tasklistdocs=FXCollections.observableArrayList();
     
     private static int  rStatus=0;//to check whether user click running state orcompleted state
     private static int  cStatus=0;//to check whether user click running state orcompleted state
  private  int taskid;
  private int projectid;
  private static String projectname;
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        System.out.println("task status update called");

                taskUpdateBtn.setOnAction(e->{
                    System.out.println("task update bnt called");
       //if user choose completed status then user mus upload file and write some summary about task;
              
                    
                
                });
                
        
    }
    public void setData(TaskModel task, ProjectTaskController controller) {
        System.out.println("set data task status update called");
        // filling data in form

        // getting the name of project from server
        System.out.println("the project id" + task.getProjectId());
        this.projectid=task.getProjectId();
        this.taskid=task.getTaskId();
         projectname = ProjectDao
                .getProjectNameThroughId(task.getProjectId());
        // getting the list of all file of that task via task id
        System.out.println("the project name s " + projectname);
        System.out.println("task ttike" + task.getTaskId());

        projectName.setText(projectname);
        taskDeadlineDate.setText(task.getTaskDeadLine());
        taskAssignDate.setText(task.getTaskCreationDate());
        taskDesc.setText(task.getTaskDescription());
        taskTitle.setText(task.getTaskName());

        list = ProjectDao.getTaskFileThroughId(task.getTaskId());
        for (String st : list) {
            System.out.println("the file " + st);
        }
        olist = FXCollections.observableArrayList(list);

        docsList.setItems(olist);

        // conversion of string to date format
        
        
        
        DateTimeFormatter df = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTime jodaDate1 = df.parseDateTime(task.getTaskDeadLine());
        System.out.println("dead date" + jodaDate1);
        DateTime now = new DateTime();
        System.out.println("present date" + now);

        // calculating day between
        int dayRem = Days.daysBetween(now, jodaDate1).getDays() + 1;

        taskDayLeft.setText(dayRem + " Days Left");

        docsDownloadBtn.setOnAction(e -> {
            // downloading task docs started

            // requesting server to download the docs of task using task id
            tmp.getOut().println("downloadTaskNote");
            tmp.getOut().println(task.getTaskId());
            tmp.getOut().println(projectname);

        });

    }

}
