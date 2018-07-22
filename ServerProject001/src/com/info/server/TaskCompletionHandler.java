package com.info.server;

import java.util.List;

import com.info.dao.ProjectDao;

public class TaskCompletionHandler extends Thread {

	
	private int taskId;
	private int projectId;
	 
	
	public TaskCompletionHandler(int taskId, int projectId) {
		this.taskId=taskId;
		this.projectId=projectId;
	}
	public void run() {
		System.out.println("task completionHandler called");
		//updating task status as "in review phase"
		ProjectDao.setTaskStatus(taskId,"In Review");
		
		//sending notification to project manager
		
		String ManagerEmail=ProjectDao.getEmailAddressOfProjectManage(projectId);
		System.out.println("the manager of email"+ManagerEmail);
		
		//getting information about task through id
		List<String> taskInfo=ProjectDao.getTaskInformation(taskId);
		for(String st:taskInfo) {
			System.out.println(st);
		}
		String taskName=taskInfo.get(0);
		String userName=taskInfo.get(1);
		String taskdesc=taskInfo.get(2);
		String taskDeadline=taskInfo.get(3);
		String taskAssigndate=taskInfo.get(4);
		String projectName=taskInfo.get(5);
		
		
		String msg="Task Name: "+taskName+"\n"
				  + "Task Description: "+taskdesc+"\n"
				  + "Project Name: "+projectName+"\n"
				  + "Task AssignTo: "+userName+"\n"
				  + "Task AssignDate: "+taskAssigndate+"\n"
				  		+ "Task Deadline: "+taskDeadline;
		//ready to send mail to manager
		
		SendMailHandler.SendMailMethod("Task Completion", msg, ManagerEmail);
		System.out.println("send mail to");
				  
		
		
		
		
	}
}
