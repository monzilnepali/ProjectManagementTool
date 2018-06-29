package com.info.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import javafx.concurrent.Task;

public class FileUploadTask extends Task<Object> {
	private String projectName;
	private List<File> files;
	CurrentUserSingleton tmp=CurrentUserSingleton.getInstance();
	@Override
	protected Object call() throws Exception {
		System.out.println("fileupload task is called ");
		
		try {

			Socket socket=tmp.getClientSocket();
			//sending data to server
			PrintWriter out=tmp.getOut();
			out.println("upload");
			//getting response for the server 
			BufferedReader input=tmp.getReader();
			System.out.println("server response is "+input.readLine());
			//sending file in server
			
			if(input.readLine().equals("ready")) {
			BufferedOutputStream bufferout=new BufferedOutputStream(socket.getOutputStream());
			BufferedInputStream fileReader;
			
			
			for(File f:files) {
				System.out.println("inside file ");
				if(f!=null) {
					System.out.println("upload file in server");
					//sending file name
				    out.println(projectName);
				    
				    
				    
				    
				    PrintWriter out1=new PrintWriter(socket.getOutputStream(),true);
					out1.println(f.getName());
					fileReader=new BufferedInputStream(new FileInputStream(f));
					byte[] buffer=new byte[1024];
					int bytesRead=0;
					while((bytesRead=fileReader.read(buffer))!=-1) {
						bufferout.write(buffer, 0, bytesRead);
						bufferout.flush();
						
					}
					
					
				}
			}
		      
			}
			
			
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	
		return null;
	}
	public FileUploadTask(String proname,List<File> files) {
		this.projectName=proname;
		this.files=files;
		
	}

}
