package com.info.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

import com.info.model.TransferFileModel;

public class FileDownload extends Thread {
	
	
	private Socket socket;
	private BufferedReader reader;
	private ObjectInputStream objIn;
	private ClientListener clientListener;

	public FileDownload(Socket socket ,BufferedReader reader,ObjectInputStream objIn) {
		this.socket=socket;
		this.reader=reader;
		this.objIn=objIn;
	}

	public void run() {
		
		//downloading the docs of project
				//getting filename from server
		try {
				System.out.println("download method start in different thread");
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
					
					
					reader.close();
				
				System.out.println("download complete in server side");
				
			}catch(Exception e) {
				e.printStackTrace();
			}
	}
		
		
		
		
	
}
