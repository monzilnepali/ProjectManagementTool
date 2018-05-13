package com.info.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ProjectModel {

	private SimpleStringProperty project_file;
	private SimpleStringProperty project_name;
	private  Boolean selectionFlag=false;
	
	public ProjectModel(String project_file, String project_name) {
		super();
		this.project_file = new SimpleStringProperty(project_file);
		this.project_name = new SimpleStringProperty(project_name);
	}

	public String getproject_file() {
		return project_file.get();
	}

	public String getProject_name() {
		return project_name.get();
	}
	@Override
	public String toString() {
	    return this.project_name.get();
	}

	public Boolean getSelectionFlag() {
		//if tab is already selected no point to load it again
		return selectionFlag;
	}

	public void setSelectionFlag(Boolean selectionFlag) {
		this.selectionFlag = selectionFlag;
	}
}
