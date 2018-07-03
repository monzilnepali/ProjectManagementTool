package com.info.server;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {

	private static int port=9090;
	public static void main(String [] args) {
		Server server=new Server(port);
		server.start();
	}
}
