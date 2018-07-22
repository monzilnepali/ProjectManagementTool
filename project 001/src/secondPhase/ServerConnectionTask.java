package secondPhase;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.info.controller.CurrentUserSingleton;
import com.info.model.Project;

import javafx.concurrent.Task;

public class ServerConnectionTask extends Task<List<Project>> {
    static CurrentUserSingleton tmp = CurrentUserSingleton.getInstance(); 
    List<Project> projectList;
    @Override
    protected List<Project> call() throws Exception {
        updateMessage("connecting to server");
        ServerConnection();
        //connecting to server
      
        
        updateMessage("fetch data...");
        //getting project list
        getProjectList();
       updateMessage("just few second..");
       
       gettingProjectImage();
       
       
       
       return null;
       
        
        
        
        
        
        
    }

    private void gettingProjectImage() {
        System.out.println("getting projectImage called");
       for(Project pro:projectList) {
           File file=new File("D:\\client\\"+pro.getprojectTitle()+"\\"+pro.getprojectTitle()+".jpg");
           if(file.exists()) {
               System.out.println("the file exit of project"+pro.getprojectTitle());
           }else {
               System.out.println("project image doesnot exitst");
           }
           
           
       }
        
    }

    private void getProjectList() {
      System.out.println("getprojectlist called");
        List<Project> projectList=new ArrayList<>();
        tmp.getOut().println("getProjectList");
        //waiting for server response
        while(true) {
            
            try {
                Project pro=(Project)tmp.getObjIn().readObject();
                System.out.println("the project name send from server is"+pro.getprojectTitle());
                if(pro.getRoleId()==007) {
                    this.projectList=projectList;
                    break;
                    
                }else {
                    
                    projectList.add(pro);
                }
                
                
                
                
            } catch (ClassNotFoundException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            
            
            
        }
        
        
    }

    private void ServerConnection() {
        try {
            Socket socket = new Socket("localhost", 9090);
            // settingup required i/o for service
            tmp.setClientSocket(socket);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));
            tmp.setReader(reader);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            tmp.setOut(out);
            BufferedOutputStream bufferout = new BufferedOutputStream(
                    socket.getOutputStream());
            tmp.setBufferout(bufferout);

            BufferedInputStream bufferin = new BufferedInputStream(
                    socket.getInputStream());
            tmp.setBufferin(bufferin);

            ObjectOutputStream objOut = new ObjectOutputStream(
                    socket.getOutputStream());
            tmp.setObjOut(objOut);

            ObjectInputStream objIn = new ObjectInputStream(
                    socket.getInputStream());
            tmp.setObjIn(objIn);
            }catch(Exception e) {
                e.printStackTrace();
            }
        
    }

}
