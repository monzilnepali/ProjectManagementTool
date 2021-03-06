package com.info.model;

import java.io.File;
import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class TaskModel implements Serializable {
	
	private int taskId;
	private int projectId;
	private String taskName;
	private String taskDescription;
	private int taskAssignTo;
	private String taskAssignToName;
	private String taskDeadLine;
	private String taskCreationDate;
	private String taskStatus;
	private int taskStatusId;
	private String taskPriority;
	private List<File> file;
	private List<File> completedFile;
	private String summaryofTask;
	
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
	public List<File> getFile() {
		return file;
	}
	public void setFile(List<File> file) {
		this.file = file;
	}
	public String getTaskCreationDate() {
		return taskCreationDate;
	}
	public void setTaskCreationDate(String taskCreationDate) {
		this.taskCreationDate = taskCreationDate;
	}
	public List<File> getCompletedFile() {
		return completedFile;
	}
	public void setCompletedFile(List<File> completedFile) {
		this.completedFile = completedFile;
	}
	public String getSummaryofTask() {
		return summaryofTask;
	}
	public void setSummaryofTask(String summaryofTask) {
		this.summaryofTask = summaryofTask;
	}
    public int getTaskStatusId() {
        return taskStatusId;
    }
    public void setTaskStatusId(int taskStatusId) {
        this.taskStatusId = taskStatusId;
    }
	
	
	
	
	
	
	
	

}
