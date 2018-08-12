package secondPhaseController;

import java.io.File;
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
import com.info.controller.ProjectCreationController;
import com.info.controller.ProjectTaskController;
import com.info.controller.Sound;
import com.info.dao.ProjectDao;
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
   
    @FXML private Pane notificationPane;
    @FXML private FontAwesomeIconView  closeNotification;
    @FXML private Label NotificationTitle;
    @FXML private Label NotificationMsg;
    @FXML private FontAwesomeIconView projectRefreshBtn;//after creation of project,
    @FXML private Label project_file;
    @FXML  private FontAwesomeIconView projectRequestBtn;

    @FXML  private FontAwesomeIconView notificationBtn;
    @FXML  private Pane notificationList;
    @FXML  private Pane projectRequestList;
    @FXML private VBox notificationBox;
    @FXML  private FontAwesomeIconView projectRequestCloseBtn;
    @FXML  private FontAwesomeIconView notificationCloseBtn;
    @FXML  private VBox projectRequestBox;

    private static int activeProjectId;
    static CurrentUserSingleton tmp = CurrentUserSingleton.getInstance(); 
    static private List<Label> labelArray;
  private  JFXSnackbar snackbar;
  private static Stage layerStage;
  static List<Project> projectList;
  static private Boolean rflag=false;//to fixing the position of review phase; 
  static private Boolean pflag=false;//active project click twice 
  String styleActive;
  String styleInActive;
  String notifyActive;//notification bell color change when notificcation appear
  String notifyInActive;
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        System.out.println("new Home Controller called");
        task_inReview.setVisible(false);
        notificationList.setVisible(false);
        projectRequestList.setVisible(false);
        notify.setVisible(false);
        Label  lb=new Label();
        lb.setText("notification");
       lb.setId("notificationLabel");
        notificationBox.getChildren().add(lb);
        activeTab=project_name;
        labelArray=new ArrayList<Label>();
        notificationPane.setVisible(false);
         clientListenerStart();
         
         //css definition
         styleActive="-fx-border-color:#2196F3;" + 
                 "    -fx-border-width:0.0 0 0 3;"
                 + "-fx-background-color:#282A2E ";
         styleInActive="-fx-border-color:transparent;" + 
                 "    -fx-border-width:0.0 0 0 3;"
                 + "-fx-background-color:transparent ";
         
         notifyActive="-fx-fill:#2196F3;";
         notifyInActive="-fx-fill:white;";
         
         
         //end
       
         notificationCloseBtn.setOnMouseClicked(e->{
             System.out.println("close notification");
             notificationBtn.setStyle("notifyInActive");
             notificationList.setVisible(false);
            
         });
         projectRequestCloseBtn.setOnMouseClicked(e->{
             
             projectRequestList.setVisible(false);
             System.out.println("close request project");
         });
        
        window_minimize.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                tmp.getStage().setIconified(true);
            }
        });
        window_close.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent me) {
                //saving the current active project id in file in json format so that when client again logged in software will show previous active project 
                //as currently active project
                if(activeProjectId!=0) {
                JSONObject obj=new JSONObject();
                obj.put("activeProject", activeProjectId);
              
                try(FileWriter file=new FileWriter("./"+tmp.getVuser().getUser_name()+".json")) {
                    file.write(obj.toJSONString());
                    file.flush();
                    
                }catch(IOException e) {
                    e.printStackTrace();
                }
                
                System.out.println("obj is"+obj);
                }
                //logout
                tmp.getOut().println("userLogout");
                // sending user
                tmp.getOut().println(tmp.getVuser().getUser_id());
                
               tmp.getStage().close();
               System.exit(0);
            }
        });
        
        projectRequestBtn.setOnMouseClicked(e->{
            
            System.out.println("projectRequestBtn clicked");
            notificationList.setVisible(false);
            projectRequestList.setVisible(true);
        });
        notificationBtn.setOnMouseClicked(e->{
            System.out.println("notificationBtn clicked");
            notificationList.setVisible(true);
            projectRequestList.setVisible(false);
        });
        
        
        project_info.setOnMouseClicked(e->{
           
           
            currentTab.setText("# PROJECT INFORMATION");
            project_info.setStyle(styleActive);
            activeTab.setStyle(styleInActive);
            
            activeTab=project_info;
            tmp.setActiveTab(activeTab.getText());
            tabClickedAction("ProjectInformation");
        });
        project_team.setOnMouseClicked(e->{
          
           if(activeTab!=project_team) {
            currentTab.setText("# PROJECT TEAM");
            project_team.setStyle(styleActive);
            activeTab.setStyle(styleInActive);
            
            activeTab=project_team;
            tmp.setActiveTab(activeTab.getText());
            tabClickedAction("ProjectTeam");
           }
        });
        
       
        task_notRunning.setOnMouseClicked(e->{
            
            if(activeTab!=task_notRunning) {
                System.out.println("task notrunning called");
          
             currentTab.setText("# NOT RUNNING");
             task_notRunning.setStyle(styleActive);
             activeTab.setStyle(styleInActive);
            
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
                task_running.setStyle(styleActive);
                activeTab.setStyle(styleInActive);
                
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
                task_inReview.setStyle(styleActive);
                activeTab.setStyle(styleInActive);
                
                activeTab=task_inReview;
                System.out.println("the active tab name is"+activeTab.getText());
                tmp.setActiveTab(activeTab.getText());
                
                
                tabClickedAction("ProjectTask1");
             
             
            }
         });
        
        task_completed.setOnMouseClicked(e->{
            if(activeTab!=task_completed) {
            currentTab.setText("# TASK COMPLETED");
            task_completed.setStyle(styleActive);
            activeTab.setStyle(styleInActive);
            
            activeTab=task_completed;
            tmp.setActiveTab(activeTab.getText());
             tabClickedAction("ProjectTask1");
            
            }
         });
        
        
        
        project_file.setOnMouseClicked(e->{
            if(activeTab!=project_file) {
            currentTab.setText("# PROJECT FILE");
            project_file.setStyle(styleActive);
            activeTab.setStyle(styleInActive);
            
            activeTab=project_file;
            tmp.setActiveTab(activeTab.getText());
             tabClickedAction("ProjectFile");
            
            }
         });
        
        
      
        projectRefreshBtn.setOnMouseClicked(e->{
            System.out.println("projectiamge refresh");
            refreshProjectList();
            
        });
        closeNotification.setOnMouseClicked(e->{
            closeNotificationHandler();            
           
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
                String msg[]=newValue.split(",");
                
                if (oldValue.equals("downloadCompleted")) {
                    //ProjectInformationController.showDialog(newValue);
                } else if(msg[0].equals("taskUpdateAck")) {
                    showNotification(newValue);
                }else if(msg[0].equals("projectCreationCompleted")) {
                    //loading the project image file
                    showNotification(newValue);
                    System.out.println("getting list of project file asssoaciate with current user");
                    refreshProjectList();
                    layerStage.close();
                    System.out.println("loading new project files");
                    loadProjectImage();
                    System.out.println("project creation stage closed");
                }else if(msg[0].equals("projectRequest")) {
                    //project request from other
                    //showing this in requestpane
                    System.out.println("project request");
                    Label lb=new Label();
                    lb.setText(msg[1]+"id is"+msg[2]);
                    lb.setId("notificationLabel");
                    lb.setOnMouseClicked(e->{
                        System.out.println("the of this project is"+msg[2]);
                    });
                    projectRequestBox.getChildren().add(lb);
                    showNotification(msg[1]);
                    
                }
                
                
                else {
                    
                    notificationBtn.setStyle("notifyActive");
                    System.out.println("uer presence online");
                    Label  lb=new Label();
                    lb.setText(newValue);
                    lb.setId("notificationLabel");
                    notificationBox.getChildren().add(lb);
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
       if(projectList!=null) {
       this.projectList=projectList;
        labelArray.add(project_info);
        labelArray.add(project_team);
        labelArray.add(task_running);
        labelArray.add(task_completed);
      
        
        
        
       
        //showing the previous active project 
        //reading the json file
        JSONParser parser=new JSONParser();
        try {
            Object obj=parser.parse(new FileReader("./"+tmp.getVuser().getUser_name()+".json"));
            JSONObject jObj=(JSONObject)obj;
            Long previousProject=(Long)jObj.get("activeProject");
            System.out.println("the previous project is"+previousProject);

            activeProjectId=previousProject.intValue();
            tmp.setActiveProjectId(activeProjectId);
           
            tabClickedAction("ProjectInformation");
            
            currentTab.setText("# PROJECT INFORMATION");
            project_info.setStyle(styleActive);
            activeTab.setStyle(styleInActive);
            activeTab=project_info;
            
         
            
            
            
            
        } catch (IOException | ParseException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        
        loadProjectImage();
      
       

       }else {
           System.out.println("no project found");
           System.out.println("adding project add button");
           addProjectButton();
       }
       
      
        
        
    }
    private void loadProjectImage() {
        System.out.println("***load project Image called");
        projectListPane.getChildren().clear();
       int flag=0;//to check whether the previous active project available or not,if not we just make first project as initial active project
        //searching the project via is
     
        for(Project pro:projectList) {
          
            System.out.println("comparing current project id"+pro.getProjectId()+"and previous active project id"+activeProjectId);
            if(pro.getProjectId()==activeProjectId) {
                System.out.println("previous project id"+activeProjectId+"is available");
                project_name.setText(pro.getprojectTitle());
                tmp.setCurrentUserRoleInActiveProject(pro.getRoleId());
                System.out.println("current user role in current project is"+pro.getRoleId());
                if(pro.getRoleId()==1) {
                    activeUserRole.setText("MANAGER");
                    rflag=true;
                    System.out.println("active user is manager");
                    task_inReview.setLayoutX(task_running.getLayoutX());
                    task_inReview.setLayoutY(task_running.getLayoutY()+40);
                    task_completed.setLayoutX(task_running.getLayoutX());
                    task_completed.setLayoutY(task_running.getLayoutY()+80);
                    task_inReview.setVisible(true);
                }else {
                    activeUserRole.setText("TEAM MEMBER");
                }
                flag=0;
                break;
            }else {
              flag++;
                
            }
            
        }
            if(flag!=0) {
                System.out.println("previous active project not avaiable");
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
                   System.out.println("the active previous project id is"+activeProjectId);
                     colorAdjust.setBrightness(-0.5);
                     imageView[imageCount].setEffect(colorAdjust);
                 }
                 
              
                 imageView[imageCount].setOnMouseClicked(e->{
                    //finding the index of previous active project from project list
                     int j;
                     for(j=0;j<size;j++) {
                         if(projectList.get(j).getProjectId()==activeProjectId) {
                             //same project clicke twice so do nothing
                             System.out.println("previous active project id is"+activeProjectId);
                             pflag=true;
                             break;
                         }
                     }
                     
                     if(pflag) {
                         //if alrady active project id clicked then we dont have to load it again 
                     pflag=false;
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
                             if(activeProject.getRoleId()==1) {
                                 activeUserRole.setText("MANAGER");
                                 System.out.println("active user is manager");
                                 if(!rflag) {
                                     System.out.println("mananger ui  already set");
                                 task_inReview.setLayoutX(task_running.getLayoutX());
                                 task_inReview.setLayoutY(task_running.getLayoutY()+40);
                                 task_completed.setLayoutX(task_running.getLayoutX());
                                 task_completed.setLayoutY(task_running.getLayoutY()+80);
                                 }
                                 task_inReview.setVisible(true);
                                 
                             }else {
                                 activeUserRole.setText("TEAM MEMBER");
                                 task_inReview.setVisible(false);
                                 task_completed.setLayoutY(task_running.getLayoutY()+40);
                             }
                             
                             tabClickedAction("ProjectInformation");
                             currentTab.setText("# PROJECT INFORMATION");
                            
                             if(activeTab!=null) {
                           activeTab.setStyle(styleInActive);
                             }
                             project_info.setStyle(styleActive);
                             activeTab=project_info;
                             
                             //searching the project name
                            System.out.println(projectList.get(i).getprojectTitle());
                            project_name.setText(projectList.get(i).getprojectTitle().toUpperCase());
                             break;
                         }
                         
                     }
                     
                     }
                     
                     //
                    
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
        
        addProjectButton();
        
         
        
        
    }
    
    private void addProjectButton() {
        
        
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
                  FXMLLoader loader=new FXMLLoader();
                  loader.setLocation(getClass().getResource("/application/ProjectCreation.fxml"));
               loader.load();
               ProjectCreationController controller=loader.getController();
               controller.setData(this);
               Parent p=loader.getRoot();
               
                  
                  Scene scene=new Scene(p);
                  Stage stage=new Stage();
                  stage.setScene(scene);
                  stage.setTitle("Project Creation");
                  stage.initModality(Modality.WINDOW_MODAL);
                  stage.initOwner(tmp.getStage());
                  layerStage=stage;
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
    private void refreshProjectList() {
        
      refreshProjectListHandler();
   
        
    }
    

    
    
    private void refreshProjectListHandler() {
       List<Project> projectListNew=ProjectDao.getProjectNameViaUserId(tmp.getVuser().getUser_id());
       if(projectList!=null) {
       projectList.clear();
       }
       projectList=projectListNew;
        
        
    }
    private void showNotification(String msg) {
        Sound.play("src\\Resource\\sound\\notify.wav");
        NotificationTitle.setText("NOTIFICATION");
        NotificationMsg.setText(msg);
        notificationPane.setVisible(true);
       
  
    }
    private void closeNotificationHandler() {
       
        FadeTransition fadeTransition=new FadeTransition();
        fadeTransition.setDuration(Duration.millis(500));
        fadeTransition.setNode(notificationPane);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();
    }

}
