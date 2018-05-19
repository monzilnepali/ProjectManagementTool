package com.info.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.info.model.User;

import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;

public class TaskAddController implements Initializable {

	private ChoiceBox<User> teamMemberList;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
			System.out.println("task add controller callled");
		
	}
	
	public void setData(int currentProjectId) {
		System.out.println("currentProjectId"+currentProjectId);
		
		
	}

}
