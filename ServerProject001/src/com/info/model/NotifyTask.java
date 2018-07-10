package com.info.model;

import java.io.Serializable;

@SuppressWarnings("serial")
public class NotifyTask  implements Serializable {

	private int taskId;
	private String taskName;
	private String projectName;
	private String deadline;
	

	public NotifyTask(int taskId,String taskName,String projectName,String deadline) {
		this.taskId=taskId;
		this.taskName=taskName;
		this.projectName=projectName;
		this.deadline=deadline;
	}




	public int getTaskId() {
		return taskId;
	}




	public String getTaskName() {
		return taskName;
	}




	public String getProjectName() {
		return projectName;
	}




	public String getDeadline() {
		return deadline;
	}
	
}
