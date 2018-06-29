package com.server;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;
import java.util.StringTokenizer;

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
		    }else {
		    	//System.out.println("nothing to called");
		    }
		}
			
		
			
		}catch(Exception e) {
			e.printStackTrace();
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
    	//sending acknowlege to server
    
    	this.out.println("ready");
    	InputStream inputByte=socket.getInputStream();
    	BufferedInputStream input1=new BufferedInputStream(inputByte);
        String path=input.readLine();
        System.out.println("path of file send from client is"+path);
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
}
