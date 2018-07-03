package com.server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread {

	private int serverPort;
	private ArrayList<ServerWorker> workerList=new ArrayList<>();//storing list of all active user so that we can message to them

	public Server(int serverPort) {
		this.serverPort=serverPort;
	}
	public List<ServerWorker> getServerWorker(){
		return workerList;
	}
	
	public void run() {
		
		try {
			ServerSocket serversocket=new ServerSocket(serverPort);
			Boolean stop=false;
			while(!stop) {
				System.out.println("waiting for client to connect");
				Socket clientSocket=serversocket.accept();
			
				PrintWriter out=new PrintWriter(clientSocket.getOutputStream(),true);
				BufferedReader input=new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			
				ServerWorker client=new ServerWorker(this,clientSocket,out,input);
				
				
				workerList.add(client);
				client.start();
			} 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
