package com.info.controller;

import java.util.List;

import com.info.controller.CurrentUserSingleton;

import javafx.concurrent.Task;

public class SendMailWorkerClient extends Task<Object> {
	
	
	
	private List<String> teamMemberEmailList ;
	CurrentUserSingleton tmp=CurrentUserSingleton.getInstance();	

	public SendMailWorkerClient(List<String> teamMemberEmailList) {
		
		
		this.teamMemberEmailList =teamMemberEmailList;
	}
	
	
	@Override
	protected Object call() throws Exception {
	
		int listSize=teamMemberEmailList.size();
		
	    for(String mail:teamMemberEmailList) {
	    	System.out.println("sending mail to from client side"+mail);
				tmp.getOut().println(mail);	
		
	    }
	    tmp.getOut().println("stop");
		
		
		
		
		return null;
	}

}
