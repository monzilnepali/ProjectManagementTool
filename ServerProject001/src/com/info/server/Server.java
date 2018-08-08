package com.info.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Server extends Thread {

    private int serverPort;
    private ArrayList<ServerWorker> workerList = new ArrayList<>();// storing
                                                                   // list of
                                                                   // all active
                                                                   // user so
                                                                   // that we
                                                                   // can
                                                                   // message to
                                                                   // them

    public Server(int serverPort) {
        this.serverPort = serverPort;
    }

    public List<ServerWorker> getServerWorker() {
        return workerList;
    }

    public void run() {

        try {
            ServerSocket serversocket = new ServerSocket(serverPort);

            System.out.println("Server started..");
            System.out.println("waiting for client to connect");
            while(true) {

                Socket clientSocket = serversocket.accept();
                System.out.println("client connected"+clientSocket.getLocalPort());

                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                BufferedReader input = new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
                ObjectOutputStream objOut = new ObjectOutputStream(
                        clientSocket.getOutputStream());
                ObjectInputStream objIn = new ObjectInputStream(
                        clientSocket.getInputStream());
                BufferedInputStream bufferin = new BufferedInputStream(
                        clientSocket.getInputStream());
                BufferedOutputStream bufferout = new BufferedOutputStream(
                        clientSocket.getOutputStream());
                ServerWorker client = new ServerWorker(this, clientSocket, out, input,
                        objOut, objIn, bufferin, bufferout);

                workerList.add(client);

                client.start();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
