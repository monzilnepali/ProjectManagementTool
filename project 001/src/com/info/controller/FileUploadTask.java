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
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;

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
		tmp.getOut().println("start");
		
		DataOutputStream dos = new DataOutputStream(tmp.getClientSocket().getOutputStream());
		BufferedInputStream fileReader=new BufferedInputStream(new FileInputStream(f));
		byte[] buffer = new byte[1024];
		int byteRead=0;
		while((byteRead=fileReader.read(buffer))!=-1) {
			dos.write(buffer,0,byteRead);
			dos.flush();
			System.out.println("uploading "+byteRead);
		}
		
		fileReader.close();
		
	
		System.out.println("uploading file completed");
		
		
	}
	public FileUploadTask(String proname,List<File> files) {
		this.projectName=proname;
		this.files=files;
		
	}

}
