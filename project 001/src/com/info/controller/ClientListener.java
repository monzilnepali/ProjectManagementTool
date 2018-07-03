package com.info.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.List;

import javafx.concurrent.Task;

public class ClientListener extends Task<String> {
	private Socket socket;
	private  BufferedReader reader;
	private List<String> notifyList;

	public ClientListener(Socket socket, BufferedReader reader) {
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
					 if(msg.equals("userPresence")) {
					 
						 String msg1=reader.readLine();
			        System.out.println("server response "+ i +" is "+msg1+"");
			        updateMessage(msg1);
			       Thread.sleep(2000);//sleeping thread for sometime otherwise multiple response from server will not shown in observable list listenre
			    
					 }
			       
			       
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
	
		
	

