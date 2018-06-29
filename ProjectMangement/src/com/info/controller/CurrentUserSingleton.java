package com.info.controller;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.info.model.User;



public class CurrentUserSingleton {

	private User vuser;
	private Socket clientSocket;
	private BufferedReader reader;
	private PrintWriter out;
	
	private static  CurrentUserSingleton singleton=new CurrentUserSingleton();
	
	/* A private Constructor prevents any other
	    * class from instantiating.
	    */
	private CurrentUserSingleton() {}
	public static CurrentUserSingleton getInstance() {
		return singleton;
	}
	
	protected User getVuser() {
		return vuser;
	}
	protected void setVuser(User vuser) {
		this.vuser = vuser;
	}
	public Socket getClientSocket() {
		return clientSocket;
	}
	public void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
	public BufferedReader getReader() {
		return reader;
	}
	public void setReader(BufferedReader reader) {
		this.reader = reader;
	}
	public PrintWriter getOut() {
		return out;
	}
	public void setOut(PrintWriter out) {
		this.out = out;
	}
}
