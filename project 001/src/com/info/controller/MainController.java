package com.info.controller;

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
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ResourceBundle;

import com.info.dao.UserDao;
import com.info.model.User;
import com.jfoenix.controls.*;
import com.jfoenix.controls.JFXSnackbar.SnackbarEvent;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MainController implements Initializable {
	@FXML private StackPane rootStack;
	@FXML private Pane login_screen;
    @FXML private JFXTextField login_emailField;
    @FXML private JFXPasswordField login_passwordField;
    @FXML private JFXButton login_actionBtn;
    @FXML private Label login_signupBtn;
    @FXML private Pane signup_screen;
    //signup screen
    @FXML private JFXTextField signup_email;
    @FXML private JFXTextField signup_username;
    @FXML private JFXPasswordField signup_password;
    @FXML private JFXPasswordField signup_repassword;
    @FXML private JFXButton signup_btn;
    @FXML private Label signup_loginbtn;
	private static int flag=0;//for switching to login screen after succesfully signup
    static CurrentUserSingleton tmp = CurrentUserSingleton.getInstance(); 

	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	  
		login_screen.setVisible(true);
		signup_screen.setVisible(false);
		System.out.println("main controller called");
		
		serverConnectionSetup();
		
		
		
		JFXDialogLayout content=new JFXDialogLayout();
		
		JFXButton button=new JFXButton("okay");
		button.setStyle("-fx-background-color:#4285F4;-fx-text-fill:white;");
		button.setCursor(Cursor.HAND);
		content.setActions(button);
		JFXDialog dialog = new JFXDialog(rootStack,content,JFXDialog.DialogTransition.LEFT);
		
		button.setOnAction(e->{
			if(flag==0) {
			dialog.close();
			}else {
				dialog.close();
				switchToLoginFromSignup(e);
			}
		
		});//end of button of dialog box	
		
		login_actionBtn.setOnAction(e->{
			System.out.println("login button clicked");
            

			if(login_emailField.getText().equals("") && login_passwordField.getText().equals("")) {
				//show error message
				content.setHeading(new Text("Login-ERROR"));
				content.setBody(new Text("Please Check your Inputs"));
				dialog.show();
				
			}else {
				 if(UserDao.emailValidator(login_emailField.getText())) {
					 //validating email pattern
					 //Now checking email address and password in database
					 User currentUser=new User();
					currentUser.setUser_email(login_emailField.getText());
					 currentUser.setUser_password(login_passwordField.getText());
					
					  User vuser = null;
                    try {
                        System.out.println("sending loginHandler request to server");
                        tmp.getOut().println("loginHandler");
                        //sending user email and password for verification to server
                        tmp.getObjOut().writeObject(currentUser);
                        
                      //reading the response from server 
                        vuser = (User)tmp.getObjIn().readObject();
                    
                    }catch(Exception e1) {
                        e1.printStackTrace();
                    }
                    if(vuser!=null) {
                        System.out.println("user logged in");
                      //creating json file 
                        File file =new File("./"+vuser.getUser_name()+".json");
                      //Create the file
                        try {
                            if (file.createNewFile()){
                              System.out.println("File is created!");
                            }else{
                              System.out.println("File already exists.");
                            }
                        } catch (IOException e2) {
                            // TODO Auto-generated catch block
                            e2.printStackTrace();
                        }
                        //switching to home.fxml scene 
                        //setting current verified user in singleton class
                        CurrentUserSingleton tmp=CurrentUserSingleton.getInstance();
                        tmp.setVuser(vuser);
                        
                        FXMLLoader loader=new FXMLLoader(getClass().getResource("/secondPhaseUI/ServerConnection.fxml"));
                        try {
                           loader.load();
                       } catch (IOException e1) {
                           // TODO Auto-generated catch block
                           e1.printStackTrace();
                       }
                        Parent p=loader.getRoot();
                        Scene new_scene=new Scene(p);
                       // String css1=this.getClass().getResource("/application/application.css").toExternalForm();
                       // String css2=this.getClass().getResource("/application/Home.css").toExternalForm();
                        //new_scene.getStylesheets().addAll(css1,css2);
                       
                        Stage new_stage=(Stage)((Node)e.getSource()).getScene().getWindow();

                        new_stage.setScene(new_scene);
                        new_stage.setTitle("Project Management");
                      
                        new_stage.show();
                    }else {
                        System.out.println("error occur while login");
                    }
					 
					 
					
					
					 
				 }else {
					 //invalid email pattern show error message
					 content.setHeading(new Text("login-ERROR"));
					 content.setBody(new Text("Invalid Email Address"));
					   dialog.show();
					 
				 }
				
				
			}
			
			
		});//end of login btn
		
		login_signupBtn.setOnMousePressed(e->{
			//switching to signup screen
			//System.out.println("signup button  clicked");
			login_screen.setVisible(false);
			signup_screen.setVisible(true);
			Stage current_stage=(Stage)((Node)e.getSource()).getScene().getWindow();
			//current 780  513
			current_stage.setHeight(730);
			current_stage.setWidth(600);
			
		});
		
		signup_btn.setOnAction(e->{
			//signup action
			if(signup_email.getText().equals("") && signup_password.getText().equals("") && signup_username.getText().equals("") && signup_repassword.getText().equals("")) {
				//display error message to check input
				content.setHeading(new Text("Signup-ERROR"));
				 content.setBody(new Text("Please check your inputs"));
					dialog.show();
				
			}else {
				
				//checking email pattern
				//checking password and reenter password string
				//checking if username exist in database or not
				//ready for inserting in database
				
				if(UserDao.emailValidator(signup_email.getText())) {
					//matched
					if(signup_password.getText().equals(signup_repassword.getText())) {
						//match
						   
						if(UserDao.usernameCheck(signup_username.getText())) {
							//already exist show error message
							content.setHeading(new Text("Signup-ERROR"));
							 content.setBody(new Text("username already exist.Please try another one"));
							dialog.show();
							
						}else {
							//ready to insert data in database
							User currentUser=new User();
							currentUser.setUser_email(signup_email.getText());
							currentUser.setUser_name(signup_username.getText());
							currentUser.setUser_password(signup_password.getText());
							User vuser=UserDao.userRegister(currentUser);
							if(vuser!=null) {
								//successfully signup
								content.setHeading(new Text("Signup-Success"));
								 content.setBody(new Text("Successfully signup"));
								dialog.show();
								flag=1;
							}else {
								//show error while registeration
							}
						}
						
						
					}else {
						//show error message:doesnot match
						content.setHeading(new Text("Signup-ERROR"));
						 content.setBody(new Text("Password and ReEnter Password Doesnot Match"));
						dialog.show();
						
					}
				}else {
					//show error message invalid email format
					content.setHeading(new Text("Signup-ERROR"));
					 content.setBody(new Text("Invalid Email Address"));
					dialog.show();
				}
			}
			
		});//end of signup action
		signup_loginbtn.setOnMousePressed(e->{
			switchToLoginFromSignup(e);
		});
		
		
	}//end of initializable method
	
	
	private void serverConnectionSetup() {
	    System.out.println("serverconnection setup called");
	    //establishing connection to remote server
	    try {
            Socket socket = new Socket("localhost", 9090);
            System.out.println("connected");
            // setting up required i/o for service
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


    private void switchToLoginFromSignup(Event e) {
		
		login_screen.setVisible(true);
		signup_screen.setVisible(false);
		Stage current_stage=(Stage)((Node)e.getSource()).getScene().getWindow();
		//current 780  513
		current_stage.setHeight(560);
		current_stage.setWidth(780);
	}

}
