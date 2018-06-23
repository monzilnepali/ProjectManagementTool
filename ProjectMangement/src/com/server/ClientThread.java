package com.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientThread extends Thread{
	private Socket socket=null;
	
	public ClientThread(Socket socket) {
		this.socket=socket;
	}

	public void run() {
		
		try {
			PrintWriter out=new PrintWriter(socket.getOutputStream(),true);
			out.println("hello client");
			BufferedReader input=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String clientInput=input.readLine();
			System.out.println(clientInput);
		    if(clientInput.equals("upload")) {
		    	System.out.println("ready to upload file in server");
		    	//sending acknowlege to server
		    	PrintWriter out1=new PrintWriter(socket.getOutputStream(),true);
		    	out1.println("ready");
		    	
		    	InputStream inputByte=socket.getInputStream();
		    	BufferedInputStream input1=new BufferedInputStream(inputByte);
		    String path=input.readLine();
		    	//reading file name from client
		    	String filename=input.readLine();
		    
		    	
		    	
		    	
		    	 File file = new File("D:\\"+path);
		         if (!file.exists()) {
		             if (file.mkdir()) {
		                 System.out.println("Directory is created!");
		             } else {
		                 System.out.println("Failed to create directory!");
		             }
		         }
		         String filepath=file.getAbsolutePath();
		         System.out.println(filepath);
		    	BufferedOutputStream outputfile=new BufferedOutputStream(new FileOutputStream(file.getAbsolutePath()+"\\"+filename));
		    	byte[] buffer=new byte[1024];
		    	int bufferRead=0;
		    	System.out.println("uploading started");
		    	while((bufferRead=input1.read(buffer))!=-1) {
		    		outputfile.write(buffer,0,bufferRead);
		    		outputfile.flush();
		    	}
		    	
		    	System.out.println("upload complete");
		    	
		    }
			
			input.close();
			out.close();
			socket.close();
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
