package secondPhaseController;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.info.controller.CurrentUserSingleton;
import com.info.model.Project;

import javafx.concurrent.Task;

public class ServerConnectionTask extends Task<List<Project>> {
    static CurrentUserSingleton tmp = CurrentUserSingleton.getInstance(); 
    List<Project> projectList;
   static Boolean flag=false;//to indicate the project exist or not
    @Override
    protected List<Project> call() throws Exception {
        updateMessage("connecting to server");
        //ServerConnection();
        //connecting to server
        updateMessage("fetch data...");
        //getting project list
        getProjectList();
       updateMessage("just few second..");
       if(flag) {
       gettingProjectImage();
       }
       return projectList;
       

    }

    private void gettingProjectImage() {
        System.out.println("getting projectImage called");
        int i=0;
        Boolean exitStatus=true;
       for(Project pro:projectList) {
        //getting file name of file
           Path p=Paths.get(pro.getProjectImage());
           
           File file=new File("D:\\client\\"+pro.getprojectTitle()+"\\"+p.getFileName().toString());
           if(file.exists()) {
               System.out.println("the file exit of project"+pro.getprojectTitle());
           }else {
               exitStatus=false;
               if(i==0) {
                       tmp.getOut().println("projectImageRequest");
                       i++;
               }
               System.out.println("project image doesnot exitst is"+pro.getprojectTitle());
               //requesting to server to download that picture
               System.out.println("sending project title to server for project image");
               tmp.getOut().println(pro.getProjectId());
               
           }
             
       }
       if(!exitStatus) {
       System.out.println("sending stop to server ");
       tmp.getOut().println("stop");
       List<Project> prolist = null;
       Project pro;
       while(true) {
           //reading object
           try {
             pro=(Project)tmp.getObjIn().readObject();
            if(pro!=null) {
                System.out.println("saving profile iamge");
               prolist.add(pro);
            }else {
                System.out.println("no more profile image to downlaod");
                
                //writing file in client side in client side
                for(Project project:prolist) {
                    savingProfileImage(project);
                }

                break;
            }
        } catch (ClassNotFoundException | IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
         
           
       }
       }
        
    }

    private void savingProfileImage(Project pro) throws IOException {
       

            System.out.println("saving profile image function");
            File file = new File("D:\\client\\" + pro.getprojectTitle() );
            if (!file.exists()) {
                if (file.mkdirs()) {
                    System.out.println("directory is created");

                } else {
                    System.out.println("directory is already exist");
                }
            }
            String projectDirPath = file.getAbsolutePath();
            FileInputStream instream = null;
            FileOutputStream outstream = null;
            
            instream = new FileInputStream(pro.getProjectImageFile());
            File outFile = new File(projectDirPath + "\\" + pro.getProjectImageFile().getName());
            outstream = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];

            int length;
            /*
             * copying the contents from input stream to output stream using
             * read and write methods
             */
            while ((length = instream.read(buffer)) > 0) {
                outstream.write(buffer, 0, length);
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
               
                if(pro.getRoleId()==007) {
                    if(flag) {
                    this.projectList=projectList;
                    }else {
                        System.out.println("no projet found");
                        this.projectList=null;
                    }
                    break;
                    
                }else if(pro!=null) {
                    flag=true;
                    System.out.println("the project name send from server is"+pro.getprojectTitle());
                    projectList.add(pro);
                }

            } catch (ClassNotFoundException | IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
            
            
            
        }
        
        
    }

    

}
