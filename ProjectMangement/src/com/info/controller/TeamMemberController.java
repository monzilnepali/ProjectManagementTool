package com.info.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;

public class TeamMemberController implements Initializable {

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}
	public void setData(Integer integer,String role) {
		System.out.println("team member controller "+integer);
	}

}
