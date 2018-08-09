package secondPhaseController;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.info.controller.CurrentUserSingleton;
import com.info.model.Project;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class ServerConnectionController implements Initializable {
    @FXML
    private Label msg;

    static CurrentUserSingleton tmp = CurrentUserSingleton.getInstance();

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        System.out.println("projectImageCheckerController called");
        System.out.println("connecting to server");

        ServerConnectionTask connection = new ServerConnectionTask();

        // When completed tasks
        connection.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED,
                new EventHandler<WorkerStateEvent>() {

                    @Override
                    public void handle(WorkerStateEvent t) {
                        System.out.println("event completed");
                        // getting project list from serverConnectionTask

                        List<Project> projectList = connection.getValue();

                        // showing the home screen
                        FXMLLoader loader = new FXMLLoader();
                        loader.setLocation(
                                getClass().getResource("/secondPhaseUI/HomeNew.fxml"));
                        try {
                            loader.load();
                        } catch (IOException e1) {
                            // TODO Auto-generated catch block
                            e1.printStackTrace();
                        }
                        HomeNewController controller = loader.getController();
                        controller.setdata(projectList);
                        Parent p = loader.getRoot();
                      
                        Scene new_scene = new Scene(p);
                         String css1=this.getClass().getResource("/secondPhaseUI/HomeNew.css").toExternalForm();
                        // String
                        // css2=this.getClass().getResource("/application/Home.css").toExternalForm();
                         new_scene.getStylesheets().addAll(css1);

                        Stage new_stage = tmp.getStage();

                        Rectangle2D primaryScreenBounds = Screen.getPrimary()
                                .getVisualBounds();

                        // set Stage boundaries to visible bounds of the main
                        // screen
                        new_stage.setX(primaryScreenBounds.getMinX());
                        new_stage.setY(primaryScreenBounds.getMinY());
                        new_stage.setWidth(primaryScreenBounds.getWidth());
                        new_stage.setHeight(primaryScreenBounds.getHeight());

                        new_stage.setScene(new_scene);
                        new_stage.setTitle("Project Management");
                        new_stage.setFullScreenExitHint("");
                        new_stage.setResizable(false);
                        new_stage.setFullScreen(true);
                        new_stage.show();

                    }
                });

        new Thread(connection).start();

        msg.textProperty().bind(connection.messageProperty());

    }

}
