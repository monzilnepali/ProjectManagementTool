package com.info.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import javafx.concurrent.Task;

public class ClientListenerTest extends Thread {
	private Socket socket;
	private  BufferedReader reader;

	public ClientListenerTest(Socket socket, BufferedReader reader) {
		this.socket=socket;
		this.reader=reader;
	}



	@Override
	public void run() {
		try {
			System.out.println("listening to server response.... ");
			 
		
				 int i=0;
			   while(true) {
			
				 if(reader!=null) {
			    System.out.println("server response "+ i +" is "+reader.readLine()+"");
			    i++;
			    //return "server response"+i+"is"+reader.readLine();
		       
			    
			 }
				
			 }
			 
			
			
			}catch(Exception e) {
				e.printStackTrace();
			}
		
		
	}
	
	
	}
	
		
	

