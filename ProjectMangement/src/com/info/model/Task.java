package com.info.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Task {
	
	private SimpleIntegerProperty taskId;
	private SimpleIntegerProperty projectId;
	private SimpleStringProperty taskName;
	private SimpleStringProperty taskDescription;
	private SimpleIntegerProperty taskAssignTo;
	private SimpleStringProperty taskAssignToName;
	private SimpleStringProperty taskDeadLine;
	private SimpleStringProperty taskStatus;
	private SimpleStringProperty taskPriority;
	
	
	public int getTaskId() {
		return taskId.get();
	}
	public void setTaskId(int taskId) {
		this.taskId = new SimpleIntegerProperty(taskId);
	}
	public String getTaskName() {
		return taskName.get();
	}
	public void setTaskName(String taskName) {
		this.taskName = new SimpleStringProperty(taskName);
	}
	public String getTaskDescription() {
		return taskDescription.get();
	}
	public void setTaskDescription(String taskDescription) {
		this.taskDescription = new SimpleStringProperty(taskDescription);
	}
	public int getTaskAssignTo() {
		return taskAssignTo.get();
	}
	public void setTaskAssignTo(int taskAssignTo) {
		this.taskAssignTo = new SimpleIntegerProperty(taskAssignTo);
	}
	public String getTaskDeadLine() {
		return taskDeadLine.get();
	}
	public void setTaskDeadLine(String taskDeadLine) {
		this.taskDeadLine = new SimpleStringProperty(taskDeadLine);
	}
	public String getTaskStatus() {
		return taskStatus.get();
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = new SimpleStringProperty(taskStatus);
	}
	public String getTaskPriority() {
		return taskPriority.get();
	}
	public void setTaskPriority(String taskPriority) {
		this.taskPriority = new SimpleStringProperty(taskPriority);
	}
	public int getProjectId() {
		return projectId.get();
	}
	public void setProjectId(int projectId) {
		this.projectId = new SimpleIntegerProperty(projectId);
	}
	public String getTaskAssignToName() {
		return taskAssignToName.get();
	}
	public void setTaskAssignToName(String taskAssignToName) {
		this.taskAssignToName = new SimpleStringProperty(taskAssignToName);
	}
	
	
	
	
	
	

}
