package com.info.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

import javafx.concurrent.Task;

public class ClientListenerTest extends Task<String> {
	private Socket socket;
	private  BufferedReader reader;
	private List<String> notifyList;

	public ClientListenerTest(Socket socket, BufferedReader reader) {
		this.socket=socket;
		this.reader=reader;
	}



	@Override
	public String call() {
		try {
			System.out.println("listening to server response.... ");
			 
		
				 int i=0;
			   while(true) {
			
				 if(reader!=null) {
					 String msg=reader.readLine();
			        System.out.println("server response "+ i +" is "+msg+"");
			        updateMessage("server response "+ i +" is "+msg+"");
			     Thread.sleep(2000);
			    
			       
			       
			       
			    if(msg.equals("notify")) {
			    	System.out.println("notification receive");
			    	System.out.println("the notification:"+reader.readLine());
			    }
			    i++;
			  
			    //return "server response"+i+"is"+reader.readLine();
		       
			    
			 }
				
			 }
			 
			
			
			}catch(Exception e) {
				e.printStackTrace();
			}
		return null;
		
		
	}
	
	
	}
	
		
	

