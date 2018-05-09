package com.info.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class MainController implements Initializable {
	@FXML private StackPane rootStack;
	@FXML private Pane login_screen;
    @FXML private JFXTextField login_emailField;
    @FXML private JFXPasswordField login_passwordField;
    @FXML private JFXButton login_actionBtn;
    @FXML private Label login_signupBtn;
    @FXML private Pane signup_screen;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		System.out.println("main controller called");
		
		JFXDialogLayout content=new JFXDialogLayout();
		content.setHeading(new Text("ERROR"));
		content.setBody(new Text("the body of JFX dialog box"));
		JFXButton button=new JFXButton("okay");
		button.setStyle("-fx-background-color:#4285F4;-fx-text-fill:white;");
		button.setCursor(Cursor.HAND);
		content.setActions(button);
		
		JFXDialog dialog = new JFXDialog(rootStack,content,JFXDialog.DialogTransition.LEFT);
		button.setOnAction(e->{
			dialog.close();
		});
		
		login_actionBtn.setOnAction(e->{
			System.out.println("login button clicked");
			dialog.show();
			
		});
	}

}
