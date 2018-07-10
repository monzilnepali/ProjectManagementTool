package com.info.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import com.info.dao.ProjectDao;
import com.info.dao.TaskDao;
import com.info.model.TransferFileModel;
import com.info.model.Project;
import com.info.model.TaskModel;

public class ServerWorker extends Thread {
	private Socket socket = null;
	private int currentUserId;
	private String currentUserName;

	private String[] clientInputArray;
	private Server server;
	private PrintWriter out;
	private BufferedReader input;
	private ObjectInputStream ObjIn;
	private BufferedInputStream bufferin;
	private BufferedOutputStream bufferout;
	private ObjectOutputStream objOut;

	public ServerWorker(Server server, Socket socket, PrintWriter out,
			BufferedReader input, ObjectOutputStream objOut,
			ObjectInputStream objIn, BufferedInputStream bufferin,
			BufferedOutputStream bufferout) {
		this.server = server;
		this.socket = socket;
		this.out = out;
		this.input = input;
		this.ObjIn = objIn;
		this.bufferin = bufferin;
		this.bufferout = bufferout;
		this.objOut = objOut;
	}

	public void run() {

		try {
			// System.out.println("sending hello client to client ");

			// out.println("hello client");
			while (true) {
				String clientInput = input.readLine();

				if (clientInput.equals("upload")) {
					System.out.println("file uploader called ---->");
					FileUploadHandler();
					System.out.println("file uploader completed");

				} else if (clientInput.equals("loginHandler")) {
					System.out.println("login handler called--->");
					loginHandler();
				} else if (clientInput.equals("sendMail")) {
					System.out.println("mail send called");
					// sending mail to all team member
					// sending notification to online user

					SendMail();

				} else if (clientInput.equals("TaskAdd")) {
					System.out.println("task add called in server side");
					TaskAddHandler();
				} else if (clientInput.equals("docsDownload")) {
					System.out.println("docs download request from client");
					docsTransferHandler();
				} else if (clientInput.equals("ProjectNameViaId")) {
					// getting response from server
					this.out.println("respProjectName");
				} else if (clientInput.equals("downloadTaskNote")) {
					System.out.println("download task note request");
					transferTaskDocsHandler();

				}else if(clientInput.equals("projectCreation")) {
					System.out.println("project creation in server side called");
					projectCreationHandler();
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void projectCreationHandler() throws NumberFormatException, IOException, ClassNotFoundException {
		// creation of project 
		int managerId=Integer.valueOf(this.input.readLine());
		
		System.out.println("the manage id "+managerId);
		Project newProject=(Project)this.ObjIn.readObject();
		System.out.println("the project information from object transfer ");
		System.out.println("the project Name is"+newProject.getprojectTitle());
		
		//storing data to server
		int projectId=ProjectDao.CreatProject(newProject, managerId);
		System.out.println("the project id "+projectId);
		if(projectId!=0) {
			//assigning status 1 for manager of this project in database
			
			ProjectDao.addTeamMember(projectId, managerId, 1, 1);
		   
			for(String team:newProject.getTeamMember()) {
				
				int teamId=ProjectDao.GetUserId(team);
				System.out.println("the team id is"+teamId);
				ProjectDao.addTeamMember(projectId, teamId, 2, 0);
				
				
			}
		
		//writing file
				FileInputStream instream = null;
				FileOutputStream outstream = null;
				
				// creating directory of that project
						File file = new File(
								"D:\\Server\\" + newProject.getprojectTitle() + "\\docs");

						if (!file.exists()) {
							if (file.mkdirs()) {
								System.out.println("directory created");
							} else {
								System.out.println("failed to create directory");
							}
						}
						String projectDirPath = file.getAbsolutePath();
						System.out.println("server dir " + projectDirPath);
						
					for(File f:newProject.getFileList()) {
						
						if(f!=null) {
							
							instream = new FileInputStream(f);
							File outFile = new File(
									projectDirPath + "\\" + f.getName());
							outstream = new FileOutputStream(outFile);
							byte[] buffer = new byte[1024];

							int length;
							/*
							 * copying the contents from input stream to output stream
							 * using read and write methods
							 */
							while ((length = instream.read(buffer)) > 0) {
								outstream.write(buffer, 0, length);
							}
							// storing the file path in database
							
							ProjectDao.setProjectFile(projectId, outFile.getAbsolutePath(), f.getName(), fileSize(f.length()));
							
							System.out.println("storing the detail of task in client device");
						}
						
					}
				
		}
		
	}

	private void transferTaskDocsHandler()
			throws NumberFormatException, IOException {
		// transfer the docs of task to client
		// getting task id
		int taskid = Integer.valueOf(this.input.readLine());
		System.out.println("the task id is" + taskid);
		String projectName = this.input.readLine();
		System.out.println("the project Name is " + projectName);
		System.out.println("getting all file from task folder");
		List<String> filePathList = TaskDao
				.getAllFileNameOfTaskThroughTaskId(taskid);
		System.out.println("file of this task");
			List<File> fileList=new ArrayList<>();
		for (String st : filePathList) {
			System.out.println("file path is" + st);
			File file = new File(st);
			if (file.exists()) {
				fileList.add(file);
				// uploading file to client side

				

			}
			if(!fileList.isEmpty()) {
				//ready to send file to client
				this.out.println("TaskFileIncoming");
				this.out.println(projectName);
				TaskModel newTask =new TaskModel();
				newTask.setFile(fileList);
				//writing the object
				
				this.objOut.writeObject(newTask);
				
				System.out.println("wrriting compleyted");
				
				
			}

		}
		System.out.println("file transfer completed");

	}

	private void docsTransferHandler() throws IOException {
		// sending the docs of project requested by team

		// getting project name

		String projectName = this.input.readLine();
		System.out.println("getting all file in docs folder of project "
				+ projectName + "in server");
		File[] files = new File("D:\\server\\" + projectName + "\\docs")
				.listFiles();

		for (File f : files) {
			if (f.isFile()) {

				this.out.println("downloadDocs");

				this.out.println(projectName);
				TransferFileModel sf = new TransferFileModel();
				sf.setFileName(f.getName());
				sf.setFileSize(f.length());

				objOut.writeObject(sf);
				System.out.println("writing object");

				System.out.println("file size is" + f.length());
				transferFileToClient(f);
			}
		}

	}

	private void transferFileToClient(File f) throws IOException {
		// sending the file f to client

		BufferedInputStream fileReader = new BufferedInputStream(
				new FileInputStream(f));
		byte[] buffer = new byte[(int) f.length()];
		int byteRead = 0;

		// sending the file size to server

		this.bufferout.write(buffer);
		System.out.println("byteRead= " + byteRead);

		System.out.println("upload complete");

	}

	private void TaskAddHandler() throws ClassNotFoundException, IOException {

		// adding task create by user to database
		System.out.println("task add handler called");

		TaskModel newTask = (TaskModel) this.ObjIn.readObject();
		FileInputStream instream = null;
		FileOutputStream outstream = null;

		// creating directory of that project
		File file = new File(
				"D:\\server\\" + newTask.getProjectId() + "\\task");

		if (!file.exists()) {
			if (file.mkdirs()) {
				System.out.println("directory created");
			} else {
				System.out.println("failed to create directory");
			}
		}
		String projectDirPath = file.getAbsolutePath();
		System.out.println("server dir " + projectDirPath);

		// passing newtask object to project dao to store in database
		int taskId = ProjectDao.addNewTask(newTask);
		if (taskId != 0) {
			// task added

			// uploading the task docs to server

			for (File f : newTask.getFile()) {
				if (f != null) {

					// writing that file to server storage
					instream = new FileInputStream(f);
					File outFile = new File(
							projectDirPath + "\\" + f.getName());
					outstream = new FileOutputStream(outFile);
					byte[] buffer = new byte[1024];

					int length;
					/*
					 * copying the contents from input stream to output stream
					 * using read and write methods
					 */
					while ((length = instream.read(buffer)) > 0) {
						outstream.write(buffer, 0, length);
					}
					// storing the file path in database
					System.out
							.println("storing the detail of task in database");
					System.out.println(
							"task id " + taskId + "and file path of server is"
									+ outFile.getAbsolutePath());
					ProjectDao.setTaskFile(taskId, outFile.getAbsolutePath(),
							f.getName(), fileSize(f.length()));

					// Closing the input/output file streams
					instream.close();
					outstream.close();

					System.out.println("File copied successfully!!");

				}
			}

			// sending to notification to online user who is assign with task
			// otherwise sending mail to the user
			String msg = "new Task From " + newTask.getTaskName();
			SendNotification(msg, newTask.getTaskAssignTo());// sending
																// notification
			// getting userEmail using user id;
			String userEmail = ProjectDao
					.getEmailThroughId(newTask.getTaskAssignTo());

			// sending mail to userEmail
			String msgEmail = "New Task\n" + "Project Name:"
					+ newTask.getProjectId() + "\n Task Name:"
					+ newTask.getTaskName();

			if (SendMailHandler.SendMailMethod("Task", msgEmail, userEmail)) {
				System.out.println("mail send");
			} else {
				System.out.println("error occur while sending mail");
			}

		}

	}

	private void SendMail() throws IOException {
		// sending mail to team member

		String projectName = this.input.readLine();
		// SendNotification(projectName);//sending notification to online user
		// only

		String msg = "invitaion from project Name-->" + projectName;
		System.out.println("the project name" + projectName);
		Boolean stop = true;
		while (stop) {
			String email = this.input.readLine();
			System.out.println("data from client is " + email);
			if (email.equals("stop")) {
				System.out.println("message from client is " + email);
				return;
			} else {
				System.out.println("email address is -->" + email);
				// sending mail

				if (SendMailHandler.SendMailMethod("Invitation", msg, email)) {
					System.out.println("email sent to -->" + email);
				} else {
					System.out.println(
							"error occur while sending mail to" + email);
				}

			}

		}

	}

	private void SendNotification(String notifyMsg, int userId) {
		// sending notification to currently active user
		System.out.println("sending notification");
		// sending notification
		List<ServerWorker> workerList = server.getServerWorker();
		for (ServerWorker worker : workerList) {
			if (worker.currentUserId == userId) {
				worker.out.println("notify");
				worker.out.println(notifyMsg);
				worker.out.flush();
			}

		}
		System.out.println("finishing sending notification to user");

	}

	private void loginHandler() throws NumberFormatException, IOException {

		this.currentUserId = Integer.valueOf(this.input.readLine());// getting
																	// currently
																	// logged in
																	// user id
		this.currentUserName = this.input.readLine();// getting currently logged
														// in username

		// System.out.println("sending msg to other active client");
		List<ServerWorker> workerList = server.getServerWorker();
		int i = 0;
		System.out.println("\n-----------------------\n");
		System.out.println("\nsending user presence notification\n");
		for (ServerWorker worker : workerList) {

			if (currentUserId != worker.currentUserId) {
				// send notification to other active user

				worker.out.println("userPresence");
				String msg = currentUserName + " is online";
				System.out.println("\nsending notification about" + msg);

				worker.out.println(msg);
				worker.out.flush();

				// get notify about all active connection in server
				this.out.println("userPresence");
				System.out.println("current user id is" + this.currentUserId);
				// this.out.println("client id "+worker.currentUserId+" iss
				// online --->"+i);
				this.out.println(worker.currentUserName + " is online");
				System.out.println("get notify about " + "client id"
						+ worker.currentUserId + "iss online--->" + i);

			}

			i++;
		}
		System.out.println("send  to other client completed");

	}

	public void FileUploadHandler() throws IOException {

		System.out.println("ready to upload file in server");
		// System.out.println("request from client is "+this.input.readLine());
		String projectName = this.input.readLine();
		System.out.println("the project Name is" + projectName);

		// creating directory of that project
		File file = new File("D:\\server\\" + projectName + "\\docs");

		if (!file.exists()) {
			if (file.mkdirs()) {
				System.out.println("directory created");
			} else {
				System.out.println("failed to create directory");
			}
		}
		String projectDirPath = file.getAbsolutePath();
		System.out.println("server dir " + projectDirPath);
		SaveFile(projectDirPath);
		System.out.println("upload complete");

	}

	private void SaveFile(String projectDirPath) throws IOException {
		// saving file to server sended from client
		int fileSize = Integer.valueOf(this.input.readLine());
		System.out.println("sending upload command to client device");

		String response = this.input.readLine();
		if (response.equals("uploadDocs")) {

			// String fileName = this.input.readLine();
			// int fileCount = Integer.valueOf(this.input.readLine());
			// System.out.println("the file count is" + fileCount);
			// BufferedOutputStream bufferout = new BufferedOutputStream(
			// new FileOutputStream(projectDirPath + "\\" + fileName));
			// byte[] buffer = new byte[fileCount];
			// int byteRead = 0;
			//
			// byteRead = this.bufferin.read(buffer);
			//
			// bufferout.write(buffer, 0, byteRead);

			System.out.println("download complete in server side");

			// moving file to another folder

		}

	}

	public static String fileSize(double size) {
		// converting bytes to KB and MB for displaying filesize of document
		// uploaded

		if (size <= 1024 * 1024) {
			return Math.round(size / 1024) + "KB";
		} else {
			return Math.round(size / (1024 * 1024)) + "MB";
		}

	}

}
