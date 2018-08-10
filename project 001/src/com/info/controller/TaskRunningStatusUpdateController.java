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

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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

public class TaskRunningStatusUpdateController implements Initializable {
    @FXML  private Pane taskinfoPane;
    @FXML private Label taskSummaryLabel;
    @FXML    private Label projectName;

    @FXML    private Label taskTitle;
    @FXML    private Label taskAssignDate;

    @FXML    private Label taskDeadlineDate;

    @FXML    private TextArea taskDesc;

    @FXML    private Label taskDayLeft;
    @FXML    private ListView<String> docsList;

    @FXML    private Button docsDownloadBtn;

    @FXML    private Button taskUpdatePane1;

    @FXML    private Pane taskCompletePane;

    @FXML    private TextArea taskSummary;

    @FXML    private ListView<String> taskFileList;

    @FXML    private Button taskUploadBtn;

    @FXML    private CheckBox taskStatus;

    @FXML    private Button taskUpdatePane2;
    private static String projectname;
    private int taskId;
    private List<String> list = new ArrayList<String>();
    ObservableList<String> olist;//task docs file 
    
    ObservableList<String>listdocs=FXCollections.observableArrayList();
    private List<File> files;
    private FileChooser fc;
    static Boolean selectionStatus;
    static CurrentUserSingleton tmp = CurrentUserSingleton.getInstance(); // current
                                                                            // user
                                                                            // object
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        taskinfoPane.setVisible(true);
        taskCompletePane.setVisible(false);
        //
        taskSummaryLabel.setVisible(false);
        taskSummary.setVisible(false);
        taskUpdatePane2.setLayoutX(180);
        taskUpdatePane2.setLayoutY(390);
        
        taskUpdatePane1.setOnAction(e->{
            //switching the pane
            taskinfoPane.setVisible(false);
            taskCompletePane.setVisible(true);
            
        });
        
        taskStatus.selectedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldValue,
                    Boolean newValue) {
               if(newValue) {
                   selectionStatus=true;
                   //if task is complete then showing user to enter task summary
                   taskSummaryLabel.setVisible(true);
                   taskSummary.setVisible(true);
                   taskUpdatePane2.setLayoutX(180);
                   taskUpdatePane2.setLayoutY(549);
                   taskUpdatePane2.setText("mark completed");
               }else {
                   taskUpdatePane2.setText("update");
                   taskUpdatePane2.setLayoutX(180);
                   taskUpdatePane2.setLayoutY(390);
                   taskSummaryLabel.setVisible(false);
                   taskSummary.setVisible(false);
               }
                
            }
            
        });
        
        
        
    }

    public void setData(TaskModel task, ProjectTaskController controller) {
        System.out.println("set data task status update called");
        

        //filechooser
          fc=new FileChooser();

        // getting the name of project from server
        System.out.println("the project id" + task.getProjectId());
       
        this.taskId=task.getTaskId();
         projectname = ProjectDao.getProjectNameThroughId(task.getProjectId());
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
        taskUploadBtn.setOnAction(e->{
            
            System.out.println("task upload btn");
            
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
            taskFileList.setItems(listdocs);//user upload file list
            }
            
        });
        
        taskUpdatePane2.setOnAction(e->{
            //marking task as completed
            if(selectionStatus) {
                //updating task as completed task
                System.out.println("mark task as completed");
                
                //checking uploaded file and task summary
                
                if(files!=null && !taskSummary.getText().equals("")) {
                    System.out.println("ready to upload task file to server");
                    TaskModel updateTask=new TaskModel();
                    updateTask.setTaskId(taskId);
                    updateTask.setSummaryofTask(taskSummary.getText());
                    updateTask.setFile(files);
                    tmp.getOut().println("UpdateTask001");
                   
                   //sending task id
                   tmp.getOut().println("completed");
                   tmp.getOut().println(tmp.getActiveProjectId());
                    
                   
                    try {
                       
                        System.out.println("sending object");
                        tmp.getObjOut().writeObject(updateTask);
                        //gettting current stage
                        Stage stage=(Stage)((Node)e.getSource()).getScene().getWindow();
                        stage.close();
                        controller.removeTask(this.taskId);
                        
                        
                    } catch (IOException e1) {
                        // TODO Auto-generated catch block
                        e1.printStackTrace();
                    }
                }else {
                    System.out.println("please check your inputs");
                }
            }else {
                //updating task file in database
                System.out.println("update the task");
                
                //task file and description
            }
        });
      
        
    }

}
