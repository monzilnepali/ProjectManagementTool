package com.info.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.commons.io.IOUtils;

import javafx.concurrent.Task;

public class FileUploadTask extends Thread {
	private String projectName;
	private List<File> files;
	private BufferedInputStream fileReader=null;
	CurrentUserSingleton tmp=CurrentUserSingleton.getInstance();
	@Override
	public void run() {
		System.out.println("fileupload task is called ");
		
		PrintWriter out=tmp.getOut();
		out.println("upload");
		//sending project to server to create folder of that project
		out.println(projectName);
		int fileSize=files.size();
		int i=1;

		for(File f:files) {
		
			
			try {
				//sending filesize
				System.out.println("sending file size to server");
				out.println(fileSize);
				
				//sending ready signal
				out.println("uploadDocs");
				UploadFile(f);
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		
		}
		
 
			
		
	
		
	}
	private void UploadFile(File f) throws IOException {
		System.out.println("uploading file");
		//uploading file to server
		tmp.getOut().println(f.getName());//sending filename to server
        DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(tmp.getClientSocket().getOutputStream()));
        byte[] done = new byte[3];
        String str = "done";  //randomly anything
        done = str.getBytes();
	    FileInputStream fis = new FileInputStream(f);
	    byte[] buf=new byte[1024];
	    int n;
	    while((n =fis.read(buf)) != -1){
	        dos.write(buf,0,n);
	       // System.out.println(n);
	      
	    }
	    System.out.println("socket is"+tmp.getClientSocket());
	    System.out.println("loop exit");	  //should i close the dataoutputstream here and make a new one each time?                 
	    dos.write(done,0,3);
	    System.out.println("sending termination to server");
	   
	    
	    fis.close();
	    return;
	
	        
	}	 
    
		       
    
		
	
	
		

	public FileUploadTask(String proname,List<File> files) {
		this.projectName=proname;
		this.files=files;
		
	}

}
