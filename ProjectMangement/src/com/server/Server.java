package com.server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main(String [] args) {
		
		try {
			ServerSocket serversocket=new ServerSocket(9090);
			Boolean stop=false;
			while(!stop) {
				System.out.println("waiting for client to connect");
				Socket clientSocket=serversocket.accept();
				ClientThread client=new ClientThread(clientSocket);
				client.start();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
