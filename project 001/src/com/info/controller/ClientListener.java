package com.info.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import com.info.model.TransferFileModel;
import com.info.model.NotifyTask;


import javafx.concurrent.Task;

public class ClientListener extends Task<String> {
	private Socket socket;
	private  BufferedReader reader;
	private List<String> notifyList;
	private ObjectOutputStream objOut;
	private ObjectInputStream objIn;

	public ClientListener(Socket socket, BufferedReader reader,ObjectOutputStream objOut,ObjectInputStream objIn) {
		this.socket=socket;
		this.reader=reader;
		this.objOut=objOut;
		this.objIn=objIn;
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
					 
						 //String msg1=reader.readLine();
						 
						 String msg1=reader.readLine();
					        System.out.println("server response "+ i +" is "+msg1+"");
					        updateMessage(msg1);
					    
			              Thread.sleep(2000);//sleeping thread for sometime otherwise multiple response from server will not shown in observable list listenre
			    
					 }
			       
			       
					 else if(msg.equals("notify")) {
			    	System.out.println("notification receive");
			    	//System.out.println("the notification:"+reader.readLine());
			    	updateMessage(reader.readLine());
			    }
					 else if(msg.equals("downloadDocs"))
					 {
						 System.out.println("downding the docs from server--->");
						
						 downloadDocs();
						 System.out.println("downloaddocs method complete  completed");
						 System.out.println("the current state of clientlistener is  "+Thread.currentThread().getState());
					
						
						 
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



	private void downloadDocs() throws IOException, ClassNotFoundException {
		//downloading the docs of project
		//getting filename from server
		System.out.println("download method start");
		String projectName=reader.readLine();
		System.out.println("the project name is"+projectName);
		System.out.println("the data from object serialization is");
		TransferFileModel cf=(TransferFileModel) objIn.readObject();
		
		System.out.println("filename is"+cf.getFileName());
		System.out.println("the filesize is "+cf.getFileSize());
		
		File file=new File("D:\\client\\"+projectName+"\\docs");
		
		
		if(!file.exists()) {
			if(file.mkdirs()) {
				System.out.println("directory created");
			}else {
				System.out.println("failed to create directory");
			}
		}
		String projectDirPath=file.getAbsolutePath();
		
		
		BufferedOutputStream bufferout=new BufferedOutputStream(new FileOutputStream(projectDirPath+"\\"+cf.getFileName()));
		   BufferedInputStream bufferin=new BufferedInputStream(socket.getInputStream());

		byte[] buffer=new byte[(int) cf.getFileSize()];
		int byteRead=0;
		
		byteRead=bufferin.read(buffer);
			
			bufferout.write(buffer, 0, byteRead);
		
		System.out.println("download complete in server side");
		
	}
	
	
	}
	
		
	

