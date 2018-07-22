package secondPhase;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.info.controller.CurrentUserSingleton;
import com.info.model.Project;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;

public class HomeNewController implements Initializable {
    
    @FXML
    private VBox projectListPane;
    static CurrentUserSingleton tmp = CurrentUserSingleton.getInstance(); 
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        System.out.println("new Home Controller called");
        

        
        
    }
    public void setdata(List<Project> projectList) {
        System.out.println("projectList called");
        
   
       
        
        //starting the clientlistener processs
        
        Image image = null;
        Image image1 = null;
        try {
            image = new Image(new FileInputStream("C:\\Users\\Manjil\\Desktop\\discord server profile.jpg"));
            image1 = new Image(new FileInputStream("C:\\Users\\Manjil\\Desktop\\discord server profile.jpg"));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }  
        System.out.println("the length of pane is"+projectListPane.getPrefWidth());
        System.out.println("the coordinate of pane is"+projectListPane.getLayoutX());
        //Setting the image view 
        ImageView imageView = new ImageView(image); 
        ImageView imageView1 = new ImageView(image1); 
        imageView.setCursor(Cursor.HAND);
         Circle glass = new Circle(70,75,50);
         Circle glass2 = new Circle(70,75,50);
        imageView.setClip(glass);
        imageView1.setClip(glass2);
        imageView.setFitHeight(150); 
        imageView.setFitWidth(150); 
        
        imageView1.setFitHeight(150); 
        imageView1.setFitWidth(150); 
       
        
        projectListPane.getChildren().add(imageView);
       
        projectListPane.getChildren().add(imageView1);
        
        
    }

}
