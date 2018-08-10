package secondPhaseController;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.info.controller.ClientListener;
import com.info.controller.CurrentUserSingleton;
import com.info.controller.ProjectTaskController;
import com.info.model.Project;
import com.jfoenix.controls.JFXSnackbar;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.FadeTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class HomeNewController implements Initializable {
    @FXML
    private AnchorPane MainPane;
    @FXML
    private FontAwesomeIconView window_minimize;

    @FXML
    private FontAwesomeIconView window_close;
    @FXML
    private VBox projectListPane;
    @FXML private Label project_name;
    @FXML   private BorderPane mainPane;
    @FXML private Label project_info;
    @FXML private Label task_running;
    @FXML private Label task_completed;
    @FXML private Label project_team;
    @FXML private Label task_inReview;
    @FXML private Label task_notRunning;
    static  private Label activeTab;
    static private String activeTabName;
    @FXML   private Label activeUserName;
    @FXML private Label notify;
    @FXML  private Label activeUserRole;
    @FXML Label currentTab;
    @FXML private FontAwesomeIconView addTaskBtn;
    @FXML private Pane notificationPane;
    @FXML private FontAwesomeIconView  closeNotification;
    @FXML private Label NotificationTitle;
    @FXML private Label NotificationMsg;
    private static int activeProjectId;
    static CurrentUserSingleton tmp = CurrentUserSingleton.getInstance(); 
    static private List<Label> labelArray;
  private  JFXSnackbar snackbar;
  static List<Project> projectList;
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        System.out.println("new Home Controller called");
        task_inReview.setVisible(false);
        activeTab=project_name;
        labelArray=new ArrayList<Label>();
        notificationPane.setVisible(false);
         clientListenerStart();
        
       
       
        
        window_minimize.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                tmp.getStage().setIconified(true);
            }
        });
        window_close.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                //saving the current active project id in file in json format so that when client again logged in software will show previous active project 
                //as currently active project
                JSONObject obj=new JSONObject();
                obj.put("activeProject", activeProjectId);
                obj.put("activeTab", activeTab.getText());
                try(FileWriter file=new FileWriter("file.json")) {
                    file.write(obj.toJSONString());
                    file.flush();
                    
                }catch(IOException e) {
                    e.printStackTrace();
                }
                System.out.println("obj is"+obj);
                //logout
                tmp.getOut().println("userLogout");
                // sending user
                tmp.getOut().println(tmp.getVuser().getUser_id());
                
               tmp.getStage().close();
               System.exit(0);
            }
        });
        project_info.setOnMouseClicked(e->{
            tabClickedAction("ProjectInformation");
           
            currentTab.setText("# PROJECT INFORMATION");
            project_info.setStyle("-fx-background-color:#36393F;");
            activeTab.setStyle("-fx-background-color:transparent;");
            activeTab=project_info;
            tmp.setActiveTab(activeTab.getText());
        });
        project_team.setOnMouseClicked(e->{
           // tabClickedAction("ProjectInformation");
           
            currentTab.setText("# PROJECT TEAM");
            project_team.setStyle("-fx-background-color:#36393F;");
           activeTab.setStyle("-fx-background-color:transparent;");
            
            activeTab=project_team;
            tmp.setActiveTab(activeTab.getText());
        });
        
       
        task_notRunning.setOnMouseClicked(e->{
            
            if(activeTab!=task_notRunning) {
                System.out.println("task notrunning called");
          
             currentTab.setText("# NOT RUNNING");
             task_notRunning.setStyle("-fx-background-color:#36393F;");
            activeTab.setStyle("-fx-background-color:transparent;");
             activeTab=task_notRunning;
             System.out.println("the active tab name is"+activeTab.getText());
             tmp.setActiveTab(activeTab.getText());
             tabClickedAction("ProjectTask1");
            }
         });
        
        
        task_running.setOnMouseClicked(e->{
            System.out.println("task running called");
            if(activeTab!=task_running) {
                
                currentTab.setText("# TASK RUNNING");
                task_running.setStyle("-fx-background-color:#36393F;");
               activeTab.setStyle("-fx-background-color:transparent;");
                
                activeTab=task_running;
                System.out.println("the active tab name is"+activeTab.getText());
                tmp.setActiveTab(activeTab.getText());
                
                
                tabClickedAction("ProjectTask1");
             
             
            }
         });
        
      
        task_inReview.setOnMouseClicked(e->{
            System.out.println("task running called");
            if(activeTab!=task_inReview) {
                
                currentTab.setText("# TASK RUNNING");
                task_inReview.setStyle("-fx-background-color:#36393F;");
               activeTab.setStyle("-fx-background-color:transparent;");
                
                activeTab=task_inReview;
                System.out.println("the active tab name is"+activeTab.getText());
                tmp.setActiveTab(activeTab.getText());
                
                
                tabClickedAction("ProjectTask1");
             
             
            }
         });
        
        task_completed.setOnMouseClicked(e->{
            if(activeTab!=task_completed) {
            currentTab.setText("# TASK COMPLETED");
            task_completed.setStyle("-fx-background-color:#36393F;");
            activeTab.setStyle("-fx-background-color:transparent;");
            
            activeTab=task_completed;
            tmp.setActiveTab(activeTab.getText());
             tabClickedAction("ProjectTask1");
            
            }
         });
        
        addTaskBtn.setOnMouseClicked(e->{
            System.out.println("task add");
            
            FXMLLoader loader=new FXMLLoader();
            loader.setLocation(getClass().getResource("/application/TaskAdd.fxml"));
            try {
                loader.load();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
           
            Parent p=loader.getRoot();
            Scene taskAddScene=new Scene(p);
            Stage taskAddStage=new Stage();
            Stage previousStage=(Stage)((Node)e.getSource()).getScene().getWindow();
            taskAddStage.setScene(taskAddScene);
            taskAddStage.setTitle("Add Task");
            taskAddStage.initModality(Modality.WINDOW_MODAL);
            taskAddStage.initOwner(previousStage);
            taskAddStage.setResizable(false);
            taskAddStage.show();
            
            
            
            
            
        });
        closeNotification.setOnMouseClicked(e->{
            closeNotification();
            
        });
        
        
        
    }
    private void clientListenerStart() {
       
        ClientListener clientlistener1 = new ClientListener();

        // binding property to task update message to get real time return
        // from Backgroundtask to update ui
        notify.textProperty().bind(clientlistener1.messageProperty());
        final ChangeListener changelistener = new ChangeListener<String>() {

            @Override
            public void changed(
                    ObservableValue<? extends String> observable,
                    String oldValue, String newValue) {

                System.out.println("the old value is " + oldValue
                        + "\nthe new value is " + newValue);
                //notifylist.setText(oldValue + "\n" + newValue);
                // notificationlist.add(newValue);

                
                if (oldValue.equals("downloadCompleted")) {
                    //ProjectInformationController.showDialog(newValue);
                } else if (newValue.equals("projectCreationCompleted")) {
                    // getProjectDetail();
                    ProjectTaskController pt=new ProjectTaskController();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    pt.loadData();
                   

                }else if(newValue.equals("taskUpdateAck")) {
                    showNotification(newValue);
                }else if(newValue.equals("projectCreationCompleted")) {
                    //loading the project image file
                    loadProjectImage();
                }
                
                else {
                    System.out.println("uer presence online");
                    showNotification(newValue);
                }

            }

        };
        notify.textProperty().addListener(changelistener);
        Thread newThread = new Thread(clientlistener1);

        newThread.setDaemon(true);
        newThread.start();
        tmp.setClientListener(newThread);
        
        
        
        
    }
    private void tabClickedAction(String fileName) {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/application/"+fileName+".fxml"));
        
        
        try {
            mainPane.setCenter(loader.load());
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
       
    }
    public void setdata(List<Project> projectList)  {
        
        activeUserName.setText(tmp.getVuser().getUser_name());
        //showing in review phase tab only for manager of the project
       
       this.projectList=projectList;
        labelArray.add(project_info);
        labelArray.add(project_team);
        labelArray.add(task_running);
        labelArray.add(task_completed);
      
        
        
        
       
        //showing the previous active project 
        //reading the json file
        JSONParser parser=new JSONParser();
        try {
            Object obj=parser.parse(new FileReader("file.json"));
            JSONObject jObj=(JSONObject)obj;
            Long previousProject=(Long)jObj.get("activeProject");
            String previousTab=(String)jObj.get("activeTab");
            System.out.println("the previous project is"+previousProject);
            System.out.println("the previous tab is"+previousTab);
            
            activeProjectId=previousProject.intValue();
            activeTabName=previousTab;
            System.out.println("the previous tab from json is "+previousTab);
            tmp.setActiveProjectId(activeProjectId);
            tmp.setActiveTab(activeTabName);
           
            
            //selecting previous active tab
            
//            for(Label lb:labelArray) {
//                
//                if(lb.getText().equals(activeTabName)) {
//                    
//                    System.out.println("inside true statement \nprevious tab="+activeTabName);
//                    System.out.println("comparing to "+lb.getText());
//                    //loading that tab fxml file
//                    
//                    if(activeTabName.equals("#  Running")) {
//                        System.out.println("# running opened");
//                        tabClickedAction("ProjectTask1");
//                    }
//                    
//                    currentTab.setText(lb.getText());
//                    lb.setStyle("-fx-background-color:#36393F;");
//                    activeTab.setStyle("-fx-background-color:transparent;");
//                    activeTab=lb;
//                    break;
//                }
//  
//            }
            tabClickedAction("ProjectInformation");
            
            currentTab.setText("# PROJECT INFORMATION");
            project_info.setStyle("-fx-background-color:#36393F;");
            activeTab.setStyle("-fx-background-color:transparent;");
            activeTab=project_info;
            
         
            
            
            
            
        } catch (IOException | ParseException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        
        loadProjectImage();
      
       

        
       
      
        
        
    }
    private void loadProjectImage() {
        System.out.println("***load project Image called");
        
        //searching the project via is
        for(Project pro:projectList) {
            
            if(pro.getProjectId()==activeProjectId) {
                project_name.setText(pro.getprojectTitle());
                tmp.setCurrentUserRoleInActiveProject(pro.getRoleId());
                if(pro.getRoleId()==1) {
                    activeUserRole.setText("MANAGER");
                    System.out.println("active user is manager");
                    task_inReview.setLayoutX(task_completed.getLayoutX());
                    task_inReview.setLayoutY(task_completed.getLayoutY());
                    task_completed.setLayoutX(task_inReview.getLayoutX());
                    task_completed.setLayoutY(task_inReview.getLayoutY()+40);
                    task_inReview.setVisible(true);
                }else {
                    activeUserRole.setText("TEAM MEMBER");
                }
              
                break;
            }else {
                //if this project is not available then we simple make first project in list as active project
                Project pro1=projectList.get(0);
                activeProjectId=pro1.getProjectId();
                tmp.setCurrentUserRoleInActiveProject(pro1.getRoleId());

                project_name.setText(pro1.getprojectTitle());
                if(pro1.getRoleId()==1) {
                    activeUserRole.setText("MANAGER");
                }else {
                    activeUserRole.setText("TEAM MEMBER");
                }
                
                
            }
            
        }
        
        System.out.println("projectList called");
        System.out.println("the no of project is "+projectList.size());
        int size=projectList.size();
        ImageView[] imageView=new ImageView[size];
        int imageCount=0;
        Image image = null;
        for(Project p:projectList) {
            System.out.println("the project image is"+p.getprojectTitle());
             try {
                 Path path=Paths.get(p.getProjectImage());
                 
                image = new Image(new FileInputStream("D:\\client\\"+p.getprojectTitle()+"\\"+path.getFileName().toString()));
            
              //Setting the image view 
                 imageView[imageCount] = new ImageView(); 
                 imageView[imageCount].setImage(image);
                 ColorAdjust colorAdjust = new ColorAdjust();
                 //dont apply color adjust to active project image
                 if(p.getProjectId()!=activeProjectId) {
                   
                     colorAdjust.setBrightness(-0.5);
                     imageView[imageCount].setEffect(colorAdjust);
                 }
                 
              
                 imageView[imageCount].setOnMouseClicked(e->{
                    //finding the index of previous active project from project list
                     int j;
                     for(j=0;j<size;j++) {
                         if(projectList.get(j).getProjectId()==activeProjectId) {
                             break;
                         }
                     }
                     ColorAdjust colorAdjust11 = new ColorAdjust();
                     colorAdjust11.setBrightness(-0.5);
                     imageView[j].setEffect(colorAdjust11);
                     
                     //searching the image object
                     ImageView image1=(ImageView) e.getSource();
                     ColorAdjust colorAdjust1 = new ColorAdjust();
                     colorAdjust1.setBrightness(0);
                     image1.setEffect(colorAdjust1);
                  
                     int i;
                     for(i=0;i<size;i++) {
                         if(image1==imageView[i]) {
                             System.out.println(i);
                             // switching between multiple project
                             System.out.println("switching between multiple project");
                             Project activeProject=projectList.get(i);
                             
                             activeProjectId=activeProject.getProjectId();
                             tmp.setActiveProjectId(activeProjectId);
                             tmp.setCurrentUserRoleInActiveProject(activeProject.getRoleId());
                             
                             tabClickedAction("ProjectInformation");
                             
                             currentTab.setText("# PROJECT INFORMATION");
                            
                             if(activeTab!=null) {
                           activeTab.setStyle("-fx-background-color:transparent;");
                             }
                             project_info.setStyle("-fx-background-color:#36393F;");
                             activeTab=project_info;
                             
                             //searching the project name
                            System.out.println(projectList.get(i).getprojectTitle());
                            project_name.setText(projectList.get(i).getprojectTitle().toUpperCase());
                             break;
                         }
                         
                     }
                     
                 
                    
                });
              
                 imageView[imageCount].setCursor(Cursor.HAND);
                 Circle glass = new Circle(57,60,45);
                 imageView[imageCount].setClip(glass);
                 imageView[imageCount].setFitHeight(120); 
                     imageView[imageCount].setFitWidth(120); 
          
                projectListPane.getChildren().add(imageView[imageCount]);
                imageCount++;
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
        
        //adding project add button
        try {
          Image  projectCreationImage = new Image(new FileInputStream("E:\\project\\ProjectManagementTool\\project 001\\src\\Resource\\UI\\icon.png"));
          ImageView imageView1=new ImageView();
          imageView1.setImage(projectCreationImage);
          imageView1.setCursor(Cursor.HAND);
     
          imageView1.setFitHeight(90); 
          imageView1.setFitWidth(90); 
          HBox hbox=new HBox();
          hbox.getChildren().add(imageView1);
          hbox.setPadding(new Insets(10,10,10,13));
        

          projectListPane.getChildren().add(hbox);
          //adding event handler to image view1
          
          imageView1.setOnMouseClicked(e->{
              System.out.println("project creation");
              
              //projectCreation fxml file
              try {
                  Parent p=FXMLLoader.load(getClass().getResource("/application/ProjectCreation.fxml"));
                  Scene scene=new Scene(p);
                  Stage stage=new Stage();
                  stage.setScene(scene);
                  stage.setTitle("Project Creation");
                  stage.initModality(Modality.WINDOW_MODAL);
                  stage.initOwner(tmp.getStage());
                  stage.show();
              } catch (IOException e1) {
                  // TODO Auto-generated catch block
                  e1.printStackTrace();
              }
          });
          
          
      } catch (FileNotFoundException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
      }
     
         
        
        
    }
    
    
    
    private void showNotification(String msg) {
        NotificationTitle.setText("NOTIFICATION");
        NotificationMsg.setText(msg);
        notificationPane.setVisible(true);
        closeNotification();
  
    }
    private void closeNotification() {
        FadeTransition fadeTransition=new FadeTransition();
        fadeTransition.setDuration(Duration.millis(4000));
        fadeTransition.setNode(notificationPane);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();
    }

}
