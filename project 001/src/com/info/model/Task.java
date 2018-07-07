package com.info.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Task implements Serializable {
	
	private int taskId;
	private int projectId;
	private String taskName;
	private String taskDescription;
	private int taskAssignTo;
	private String taskAssignToName;
	private String taskDeadLine;
	private String taskStatus;
	private String taskPriority;
	public int getTaskId() {
		return taskId;
	}
	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	public String getTaskDescription() {
		return taskDescription;
	}
	public void setTaskDescription(String taskDescription) {
		this.taskDescription = taskDescription;
	}
	public int getTaskAssignTo() {
		return taskAssignTo;
	}
	public void setTaskAssignTo(int taskAssignTo) {
		this.taskAssignTo = taskAssignTo;
	}
	public String getTaskAssignToName() {
		return taskAssignToName;
	}
	public void setTaskAssignToName(String taskAssignToName) {
		this.taskAssignToName = taskAssignToName;
	}
	public String getTaskDeadLine() {
		return taskDeadLine;
	}
	public void setTaskDeadLine(String taskDeadLine) {
		this.taskDeadLine = taskDeadLine;
	}
	public String getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	public String getTaskPriority() {
		return taskPriority;
	}
	public void setTaskPriority(String taskPriority) {
		this.taskPriority = taskPriority;
	}
	
	
	
	
	
	
	
	

}
