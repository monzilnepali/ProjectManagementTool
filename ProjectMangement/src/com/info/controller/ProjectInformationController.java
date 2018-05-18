package com.info.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;

public class ProjectInformationController implements Initializable {

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		System.out.println("project information controller called");
	}
	public void setData(String data) {
		System.out.println("data called"+data);
	}

}
