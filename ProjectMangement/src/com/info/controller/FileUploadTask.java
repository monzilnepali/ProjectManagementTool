package com.info.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
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
	private BufferedInputStream fileReader=null;
	CurrentUserSingleton tmp=CurrentUserSingleton.getInstance();
	@Override
	protected Object call() throws Exception {
		System.out.println("fileupload task is called ");
		
		PrintWriter out=tmp.getOut();
		out.println("upload");
		//sending project to server to create folder of that project
		out.println(projectName);
		int fileSize=files.size();
		int i=1;

		for(File f:files) {
		
			UploadFile(f);
			
			
		
		}
		
 
			
		
	
		return null;
	}
	private void UploadFile(File f) throws IOException {
		//uploading file to server
		tmp.getOut().println(f.getName());
		DataOutputStream dos = new DataOutputStream(tmp.getClientSocket().getOutputStream());
		FileInputStream fis = new FileInputStream(f);
		byte[] buffer = new byte[4096];
		
		while (fis.read(buffer) > 0) {
			dos.write(buffer);
		}
		System.out.println("uploading file completed");
		
		
	}
	public FileUploadTask(String proname,List<File> files) {
		this.projectName=proname;
		this.files=files;
		
	}

}
