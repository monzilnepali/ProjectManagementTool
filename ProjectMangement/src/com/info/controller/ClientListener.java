package com.info.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javafx.concurrent.Task;

public class ClientListener extends Task<String> {
	private Socket socket;
	private  BufferedReader reader;

	public ClientListener(Socket socket, BufferedReader reader) {
		this.socket=socket;
		this.reader=reader;
	}



	@Override
	protected String call() throws Exception {
		try {
			System.out.println("listening to server response.... ");
			 
		
				 int i=0;
				 String msg;
			   while(true) {
//			
//				 if(reader!=null) {
//					 String input=reader.readLine();
//			     	msg="server response "+i+" is "+input;
//				  System.out.println(msg);
//				  if(input.equals("notify")) {
//					  System.out.println("send notification");
//				  }else {
//					  System.out.println("no notification");
//				  }
//			      i++;
//			  
//			    
//			 }
				   String response=reader.readLine();
				   if(response.equals("userPresence")) {
					   System.out.println(reader.readLine());
				   }else if(response.equals("notify")) {
					   System.out.println("notificatio called");
					   System.out.println("notiifcation from system is -->"+reader.readLine());
				   }
				
			 }
			 
			
			
			}catch(Exception e) {
				e.printStackTrace();
			}
		return null;
		
	}





	
	
	}
	
		
	

