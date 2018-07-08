package com.info.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import com.info.model.User;

import javafx.scene.Parent;
import javafx.stage.Stage;



public class CurrentUserSingleton {

	private User vuser;
	private Socket clientSocket;
	private BufferedReader reader;
	private PrintWriter out;
	private BufferedOutputStream bufferout;
	private BufferedInputStream bufferin;
	private ObjectOutputStream objOut;
	private ObjectInputStream objIn;
	private Thread clientListener;
	
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
	public BufferedOutputStream getBufferout() {
		return bufferout;
	}
	public void setBufferout(BufferedOutputStream bufferout) {
		this.bufferout = bufferout;
	}
	public ObjectOutputStream getObjOut() {
		return objOut;
	}
	public void setObjOut(ObjectOutputStream objOut) {
		this.objOut = objOut;
	}
	public ObjectInputStream getObjIn() {
		return objIn;
	}
	public void setObjIn(ObjectInputStream objIn) {
		this.objIn = objIn;
	}
	public BufferedInputStream getBufferin() {
		return bufferin;
	}
	public void setBufferin(BufferedInputStream bufferin) {
		this.bufferin = bufferin;
	}
	public Thread getClientListener() {
		return clientListener;
	}
	public void setClientListener(Thread clientListener) {
		this.clientListener = clientListener;
	}
	

}
