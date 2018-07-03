package com.info.model;

import javafx.beans.property.SimpleStringProperty;

public class ProjectModel {

	private SimpleStringProperty project_file;
	private SimpleStringProperty project_name;

	
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

	
}
