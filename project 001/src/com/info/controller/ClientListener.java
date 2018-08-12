package com.info.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

import org.omg.Messaging.SyncScopeHelper;

import com.info.model.TransferFileModel;
import com.info.model.NotifyTask;
import com.info.model.Project;
import com.info.model.TaskModel;

import javafx.concurrent.Task;

public class ClientListener extends Task<String> {
	private Socket socket;
	private BufferedReader reader;
	private List<String> notifyList;
	private ObjectOutputStream objOut;
	private ObjectInputStream objIn;
	 static CurrentUserSingleton tmp = CurrentUserSingleton.getInstance(); 
	public ClientListener() {
		this.socket = tmp.getClientSocket();
		this.reader = tmp.getReader();
		this.objOut = tmp.getObjOut();
		this.objIn = tmp.getObjIn();
	}

	@Override
	public String call() {
		try {
			System.out.println("listening to server response.... ");

			int i = 0;
			while (true) {

				if (reader != null) {
					String msg = reader.readLine();
					if (msg.equals("userPresence")) {

						// String msg1=reader.readLine();

						String msg1 = reader.readLine();
						System.out.println(
								"server response " + i + " is " + msg1 + "");
						updateMessage(msg1);

						Thread.sleep(2000);// sleeping thread for sometime
											// otherwise multiple response from
											// server will not shown in
											// observable list listenre

					}

					else if (msg.equals("notify")) {
						System.out.println("notification receive");
						// System.out.println("the
						// notification:"+reader.readLine());
						updateMessage(reader.readLine());
					} else if (msg.equals("ProjectNameViaId")) {
						System.out.println(
								"getting project name from id from server");
						System.out.println("th ");

					} else if (msg.equals("TaskFileIncoming")) {
						System.out.println("task file incoming handler");
						TaskFileIncomingHandler();
						updateMessage("download completed");

					}else if(msg.equals("ProjectDocsIncoming")) {
						System.out.println("ProjectFileIncoming called in client side");
						ProjectFileIncomingHandler("docs");
					}else if(msg.equals("projectCreationCompleted")){
						System.out.println("projectCreationCompleted called");
						updateMessage(msg);
					}else if(msg.equals("taskUpdateAck")) {
					    System.out.println("taskUpdateAck called");
					    updateMessage(msg);
					}else if(msg.equals("projectRequest")) {
					    //invitation to project
					    System.out.println("project request called");
					    //reading notification messsage
					    String msg1="projectRequest,"+reader.readLine();
					    updateMessage(msg1);
					}
					i++;

					// return "server response"+i+"is"+reader.readLine();

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	private void ProjectFileIncomingHandler(String dir)
			throws IOException, ClassNotFoundException, InterruptedException {

		// downlaod the docs of project from database
		// receiving project Name
		String projectName = reader.readLine();
		System.out.println("the project Name is " + projectName);
		// getting project object from server

		Project newProject = (Project) this.objIn.readObject();
		// writing file
		FileInputStream instream = null;
		FileOutputStream outstream = null;

		// creating directory of that project
		File file = new File("D:\\Client\\"+tmp.getVuser().getUser_name()+"\\"+ projectName+"\\"+dir);

		if (!file.exists()) {
			if (file.mkdirs()) {
				System.out.println("directory created");
			} else {
				System.out.println("failed to create directory");
			}
		}
		String projectDirPath = file.getAbsolutePath();
		System.out.println("client dir " + projectDirPath);

		for (File f : newProject.getFileList()) {

			if (f != null) {
				instream = new FileInputStream(f);
				File outFile = new File(projectDirPath + "\\" + f.getName());
				outstream = new FileOutputStream(outFile);
				byte[] buffer = new byte[1024];

				int length;
				/*
				 * copying the contents from input stream to output stream using
				 * read and write methods
				 */
				while ((length = instream.read(buffer)) > 0) {
					outstream.write(buffer, 0, length);
				}
				// storing the file path in database
				System.out
						.println("storing the detail of task in client device");

			}
		}
		System.out.println("downloading task docs completed");
		updateMessage("downloadCompleted");
		Thread.sleep(2000);
		updateMessage(projectDirPath);
		

	}

	private void TaskFileIncomingHandler()
			throws IOException, ClassNotFoundException {
		// getting projectname

		String projectName = this.reader.readLine();
		System.out.println("the project Name is" + projectName);
		TaskModel newTask = (TaskModel) this.objIn.readObject();
		System.out.println("the filelsit is" + newTask.getFile());

		// writing file
		FileInputStream instream = null;
		FileOutputStream outstream = null;

		// creating directory of that project
		File file = new File("D:\\Client\\" + projectName + "\\task");

		if (!file.exists()) {
			if (file.mkdirs()) {
				System.out.println("directory created");
			} else {
				System.out.println("failed to create directory");
			}
		}
		String projectDirPath = file.getAbsolutePath();
		System.out.println("client dir " + projectDirPath);

		for (File f : newTask.getFile()) {
		    System.out.println("the file name is"+f.getName());

			if (f != null) {
				instream = new FileInputStream(f);
				File outFile = new File(projectDirPath + "\\" + f.getName());
				outstream = new FileOutputStream(outFile);
				byte[] buffer = new byte[1024];

				int length;
				/*
				 * copying the contents from input stream to output stream using
				 * read and write methods
				 */
				while ((length = instream.read(buffer)) > 0) {
					outstream.write(buffer, 0, length);
				}
				// storing the file path in database
				System.out.println(
						"storing the detail of project in client device");

			}
		}
		System.out.println("downloading task file completed");

	}

}
