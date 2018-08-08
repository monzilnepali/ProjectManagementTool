package secondPhaseController;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.info.controller.CurrentUserSingleton;
import com.info.model.Project;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class HomeNewController implements Initializable {
    @FXML
    private FontAwesomeIconView window_minimize;

    @FXML
    private FontAwesomeIconView window_close;
    @FXML
    private VBox projectListPane;
    @FXML private Label project_name;
    @FXML   private BorderPane mainPane;
    @FXML Label information_btn;
    @FXML Label currentTab;
    private static int activeProjectId;
    static CurrentUserSingleton tmp = CurrentUserSingleton.getInstance(); 
  
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        System.out.println("new Home Controller called");
        
        
       
        
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
                try(FileWriter file=new FileWriter("file.json")) {
                    file.write(obj.toJSONString());
                    file.flush();
                    
                }catch(IOException e) {
                    e.printStackTrace();
                }
                System.out.println("obj is"+obj);
                
                
               tmp.getStage().close();
               System.exit(0);
            }
        });
        information_btn.setOnMouseClicked(e->{
            //information window will open
            System.out.println("information bnt clicked");
            currentTab.setText("# INFORMATION");
           tmp.setActiveTab("information");
            FXMLLoader loader=new FXMLLoader(getClass().getResource("/secondPhaseUI/ProjectInformation.fxml"));
        
            try {
                mainPane.setCenter(loader.load());
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        });
        
        
    }
    public void setdata(List<Project> projectList)  {
        int size=projectList.size();
        //showing the previous active project 
        //reading the json file
        JSONParser parser=new JSONParser();
        try {
            Object obj=parser.parse(new FileReader("file.json"));
            JSONObject jObj=(JSONObject)obj;
            Long previousProject=(Long)jObj.get("activeProject");
            System.out.println("the previous project is"+previousProject);
            activeProjectId=previousProject.intValue();
            tmp.setActiveProjectId(activeProjectId);
           
            //searching the project via is
           for(Project pro:projectList) {
               
               if(pro.getProjectId()==activeProjectId) {
                   project_name.setText(pro.getprojectTitle());
                   tmp.setCurrentUserRoleInActiveProject(pro.getRoleId());
                   break;
               }else {
                   //if this project is not available then we simple make first project in list as active project
                   Project pro1=projectList.get(0);
                   activeProjectId=pro1.getProjectId();
                   tmp.setCurrentUserRoleInActiveProject(pro1.getRoleId());

                   project_name.setText(pro1.getprojectTitle());
                   
                   
               }
               
           }
            
            
            
            
        } catch (IOException | ParseException e2) {
            // TODO Auto-generated catch block
            e2.printStackTrace();
        }
        
        
        System.out.println("projectList called");
        System.out.println("the no of project is "+projectList.size());
      
        ImageView[] imageView=new ImageView[size];
        int imageCount=0;
        Image image = null;
        for(Project p:projectList) {
            System.out.println("the project image is"+p.getprojectTitle());
             try {
                image = new Image(new FileInputStream("D:\\client\\"+p.getprojectTitle()+"\\"+p.getprojectTitle()+".jpg"));
            
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
                             Project activeProject=projectList.get(i);
                             
                             activeProjectId=activeProject.getProjectId();
                             tmp.setActiveProjectId(activeProjectId);
                             tmp.setCurrentUserRoleInActiveProject(activeProject.getRoleId());
                             //searching the project name
                            System.out.println(projectList.get(i).getprojectTitle());
                            project_name.setText(projectList.get(i).getprojectTitle().toUpperCase());
                             break;
                         }
                         
                     }
                     
                 
                    
                });
              
                 imageView[imageCount].setCursor(Cursor.HAND);
                 Circle glass = new Circle(60,75,45);
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
        Image  projectCreationImage = new Image(new FileInputStream("addButton.jpg"));
        ImageView imageView1=new ImageView();
        imageView1.setImage(projectCreationImage);
        imageView1.setCursor(Cursor.HAND);
        Circle glass1 = new Circle(60,75,45);
        imageView1.setClip(glass1);
        imageView1.setFitHeight(120); 
        imageView1.setFitWidth(120); 
        
        
    } catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
   
       

        
       
      
        
        
    }

}
