package com.server;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.StringTokenizer;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;

public class ServerWorker extends Thread{
	private Socket socket=null;
	private int currentUserId;
	
	private String[] clientInputArray;
	private Server server;
	private PrintWriter out;
	private BufferedReader input;
	
	
	
	public ServerWorker(Server server,Socket socket, PrintWriter out, BufferedReader input) {
		this.server=server;
		this.socket=socket;
		this.out=out;
		this.input=input;
	}

	public void run() {
		
		try {
		//	System.out.println("sending hello client to client ");
		
			  out.println("hello client");
			while(true) {
			String clientInput=input.readLine();
			//System.out.println("clientInput"+ clientInput);
			//splitting clientinput
			StringTokenizer st=new StringTokenizer(clientInput,"|");
		//	out.println("hello client again");
	        clientInputArray=new String[2];
			int i=0;
			while(st.hasMoreElements()) {
			
				clientInputArray[i]=st.nextToken();
				i++;
			}
		
			//System.out.println("client 1 input:"+clientInputArray[0]);
			//System.out.println("client 2 input:"+clientInputArray[1]);
			
		    if(clientInputArray[0].equals("upload")) {
		    	System.out.println("file uploader called ---->");
		    	FileUploadHandler();
		    	
		    }else if(clientInputArray[0].equals("loginHandler")) {
		    	System.out.println("login handler called--->");
		    	loginHandler();
		    }else if(clientInputArray[0].equals("sendMail")) {
		    	System.out.println("mail send called");
		    	SendMain();
		    	
		    }
		    
		    
		    else {
		    	//System.out.println("nothing to called");
		    }
		}
			
		
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	

	private void SendMain() throws IOException {
		
		String projectName=this.input.readLine();
		String msg="invitaion from project Name-->"+projectName;
    	System.out.println("the project name"+projectName);
    	Boolean stop=true;
    	while(stop) {
    		String email=this.input.readLine();
    		System.out.println("data from client is "+email);
    		if(email.equals("stop")) {
    			System.out.println("message from client is "+email);
    			return;
    		}else {
    			System.out.println("email address is -->"+email);
    			//sending mail
    			
    			if(SendMailHandler.SendMailMethod(msg, email)) {
    				System.out.println("email sent to -->"+email);
    			}else {
    				System.out.println("error occur while sending mail to"+email);
    			}
    			
    			
    		}
    		
    		
    		
    		
    	}
    	
		
	}

	private void loginHandler() throws NumberFormatException, IOException {
		//System.out.println("login handler called");
		this.currentUserId=Integer.valueOf(clientInputArray[1]);
		 // System.out.println("currently logged user id"+clientInputArray[1]);
		
		  //sending msg to message to other online team member
		  
		//  System.out.println("sending msg to other active client");
		  List<ServerWorker> workerList=server.getServerWorker();
		  int i=0;
		  System.out.println("\n-----------------------\n");
		  System.out.println("\nsending notification\n");
		  for(ServerWorker worker:workerList) {
			  if(currentUserId!=worker.currentUserId) {
				  //send notification to othetr active user
				 
			  String msg="client id "+currentUserId+" is online-->"+i;
			  System.out.println("\nsending notification about"+msg);

			  worker.out.println(msg);
			  worker.out.flush();
			  
			//get notify about all active connection in server
			  System.out.println("get notify\n");
			 System.out.println("current user id is"+this.currentUserId);
			  out.println("client id "+worker.currentUserId+" iss online --->"+i);
			  System.out.println("get notify about "+"client id"+worker.currentUserId+"iss online--->"+i);
		  
			  }
				  
			      i++;
		  }
		  System.out.println("send  to other client completed");
		
	}

	public void FileUploadHandler() throws IOException {
		
		System.out.println("ready to upload file in server");
		//System.out.println("request from client is "+this.input.readLine());
		String projectName=this.input.readLine();
		System.out.println("the project Name is"+projectName);
		
		//creating directory of that project
		File file=new File("D:\\"+projectName);
		
	
		if(!file.exists()) {
			if(file.mkdirs()) {
				System.out.println("directory created");
			}else {
				System.out.println("failed to create directory");
			}
		}
		String projectDirPath=file.getAbsolutePath();
		
		
		    SaveFile(projectDirPath);
	    	System.out.println("upload complete");
			
			
			
			
			
		
    	
	}

	private void SaveFile(String projectDirPath) throws IOException {
		//saving file to server sended from client
		String filename=this.input.readLine();
		System.out.println("file to be upload is"+filename);
		
		
		DataInputStream dis = new DataInputStream(this.socket.getInputStream());
		FileOutputStream fos = new FileOutputStream(projectDirPath+filename);
		System.out.println("setting up buffer");
		byte[] buffer = new byte[4096];
		
		int filesize = 15123; // Send file size in separate msg
		int read = 0;
		int totalRead = 0;
		int remaining = filesize;
		while((read = dis.read(buffer, 0, Math.min(buffer.length, remaining))) > 0) {
			totalRead += read;
			remaining -= read;
			System.out.println("read " + totalRead + " bytes.");
			fos.write(buffer, 0, read);
		}
		
		
		
		
	}
	

}
