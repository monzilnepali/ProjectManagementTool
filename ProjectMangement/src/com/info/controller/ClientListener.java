package com.info.controller;

import java.io.BufferedReader;
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
			
				 if(reader!=null) {
				msg="server response "+i+" is "+reader.readLine();
			    System.out.println(msg);
			    i++;
			   System.out.println("return data to main thread");
			    return msg;
		       
			    
			 }
				
			 }
			 
			
			
			}catch(Exception e) {
				e.printStackTrace();
			}
		return null;
		
	}
	
	
	}
	
		
	

