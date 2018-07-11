package com.info.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.ResourceBundle;
import com.info.dao.ProjectDao;
import com.info.model.Project;
import com.info.model.ProjectModel;
import com.info.model.Task1;
import com.jfoenix.controls.*;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HomeController implements Initializable {
	@FXML private AnchorPane mainRoot;
	@FXML
	private JFXTreeView<ProjectModel> projectTree;
	@FXML
	private JFXTabPane home_tabPane;
	 @FXML private Label currentUserName;
     @FXML  private Label currentUserEmail;
	private static Boolean flag=false;
     private static TreeItem<ProjectModel> root;
     protected static ObservableList<String> list;
    
     @FXML private Label notify;
     @FXML private Label notifylist;
      static CurrentUserSingleton tmp=CurrentUserSingleton.getInstance();	//current user object
     private BufferedReader reader=null;
     @FXML private ListView<Task1> notificationListView;
     

     @FXML      private MenuItem projectRefreshBtn;

 	
private static  Stage CurrentStage;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		System.out.println("-------------------\n\n");
		System.out.println("home controlle called\n");
		
		JFXSnackbar snackbar=new JFXSnackbar(mainRoot);
	
	
		ObservableList<Task1> notificationlist=FXCollections.observableArrayList();
		notificationlist.add(new Task1(1,"task1 running"));
		notificationlist.add(new Task1(2,"task2 finished"));
		notificationlist.add(new Task1(3,"task3 completed"));
		
	
		notificationListView.setItems(notificationlist);
		
		projectRefreshBtn.setOnAction(e->{
			System.out.println("projectRefreshBtn clicked");
			getProjectDetail();
		});
		
		notificationListView.setOnMouseClicked(e->{
			
			System.out.println("list view clicked");
			
			int item=notificationListView.getSelectionModel().getSelectedIndex();
			Task1 data=notificationListView.getSelectionModel().getSelectedItem();
			System.out.println("select index is"+item+"\n data is"+data.getId());
			
		});		
		
		System.out.println("home controller method called,user="+tmp.getVuser().getUser_name());
		
		currentUserEmail.setText(tmp.getVuser().getUser_name());
		currentUserName.setText(tmp.getVuser().getUser_email());
		
		

		
		
		
		SingleSelectionModel<Tab> selectionModel = home_tabPane.getSelectionModel();
	

	//tree view root element which is only one
   root = new TreeItem<>(new ProjectModel("", "Project"));
	root.setExpanded(true);
	
	getProjectDetail();

	projectTree.setRoot(root);
	
	System.out.println("connecting to server");
	
	 try {		
		 
		 Socket socket=new Socket("localhost",9090);
		 //settingup required i/o for service
		 tmp.setClientSocket(socket);
		 reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
		 tmp.setReader(reader);
	   	BufferedOutputStream bufferout=new BufferedOutputStream(socket.getOutputStream());
		 tmp.setBufferout(bufferout);
		 
		 BufferedInputStream bufferin=new BufferedInputStream(socket.getInputStream());
		 tmp.setBufferin(bufferin);
		 
		 ObjectOutputStream objOut=new ObjectOutputStream(socket.getOutputStream());
		 tmp.setObjOut(objOut);
		 
		 
		 ObjectInputStream objIn=new ObjectInputStream(socket.getInputStream());
		 tmp.setObjIn(objIn);
		 
		
		 ClientListener clientlistener1=new ClientListener(socket,reader,objOut,objIn);
		 
 
		 
		 //binding property to task update message to get real time return from Backgroundtask to update ui
		  notify.textProperty().bind(clientlistener1.messageProperty());
	   	 final ChangeListener changelistener=new ChangeListener<String>() {


			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				
				System.out.println("the old value is "+oldValue+"\nthe new value is "+newValue);
				notifylist.setText(oldValue+"\n"+newValue);
				//notificationlist.add(newValue);
				
				if(oldValue.equals("downloadCompleted")) {
					ProjectInformationController.showDialog(newValue);
				}else if(newValue.equals("projectCreationCompleted")) {
					//getProjectDetail();
					CurrentStage.close();
					
					
					
				}else {
				snackbar.show(newValue, 6000);//showing notification popup from button about user online
				}
				
			}
 
			 
		 };
		 notify.textProperty().addListener(changelistener);
	  Thread newThread=new Thread(clientlistener1);
	  
	 
	  newThread.setDaemon(true);
	  newThread.start();
	  tmp.setClientListener(newThread);
		
		
		 PrintWriter out=new PrintWriter(socket.getOutputStream(),true);
		 tmp.setOut(out);
		 System.out.println("sending username to server and user id is");
		out.println("loginHandler");
		out.println(tmp.getVuser().getUser_id());//sending user id
		out.println(tmp.getVuser().getUser_name());//sending username to server
		
	} catch (UnknownHostException e2) {
		// TODO Auto-generated catch block
		e2.printStackTrace();
	} catch (IOException e2) {
		// TODO Auto-generated catch block
		e2.printStackTrace();
	}

	System.out.println("----->\noutside the thread");

	projectTree.setOnMouseClicked(e -> {
		if (e.getClickCount() == 2) {
			// show the child in tab panel on double clickt

			TreeItem<ProjectModel> item = projectTree.getSelectionModel().getSelectedItem();// getting selected  treeitem
			// dont show root item in tabview ,only its children
			if (item.getChildren().size() == 0) {

				// true means: tab is already loaded in tabview so view that tab without adding
				// in tabview again

				
				ObservableList<Tab> tabList = home_tabPane.getTabs();
				Tab currentTab = null;
				for (Tab a : tabList) {
					//System.out.println("parent "  + a.getId() + "name " + a.getText());
					if (item.getParent().toString().equals(a.getId())
							&& item.getValue().getProject_name().equals(a.getText())) {
						currentTab = a;
						flag=true;
						break;
					}
				}
				
				if(flag) {
					System.out.println("already exist changing active selectionn mode");
			   // System.out.println("tab selected currnet" + currentTab.getText());
				selectionModel.select(currentTab);// changin selected tab to present click tab
				
				flag=false;
				} else {
					System.out.println("doesnot exist adding in tabview");
				//flag=false;
				// tab is not present in tabview so load it
				Tab tab1 = new Tab();
				tab1.setId(item.getParent() + "");
				tab1.setText(item.getValue().getProject_name());
				selectionModel.select(tab1);
				
				FXMLLoader loader=new FXMLLoader();
				loader.setLocation(getClass().getResource("/application/"+item.getValue().getproject_file()+".fxml"));
				try {
					loader.load();
				} catch (IOException e1) {
					
					e1.printStackTrace();
				}
				if(item.getValue().getproject_file().equals("ProjectInformation")) {
					System.out.println("projectInformationController called 1");
				ProjectInformationController controller=loader.getController();
				
				controller.setData(Integer.valueOf(item.getParent().getValue().getproject_file()),splitString(item.getParent().getValue().getProject_name())    );
				
				}else if(item.getValue().getproject_file().equals("TeamMember")) {
					System.out.println("team member controller called 1" );
					TeamMemberController controller=loader.getController();
					//passing projectName and user id
					controller.setData(Integer.valueOf(item.getParent().getValue().getproject_file()),splitString(item.getParent().getValue().getProject_name())    );
				}
				
				else if(item.getValue().getproject_file().equals("ProjectTask")) {
					System.out.println("ProjectTask controller called 1" );
					ProjectTaskController controller=loader.getController();
					//passing projectName and user id
					controller.setData(Integer.valueOf(item.getParent().getValue().getproject_file()),splitString(item.getParent().getValue().getProject_name())    );
				}
				else if(item.getValue().getproject_file().equals("TeamCalender")) {
					System.out.println("TeamCalender controller called 1" );
					TeamCalenderController controller=loader.getController();
					//passing projectName and user id
					//controller.setData(Integer.valueOf(item.getParent().getValue().getproject_file()),splitString(item.getParent().getValue().getProject_name())    );
				}
				
				
				Parent p =loader.getRoot();
				
				
				tab1.setContent(p);
				String rootItem = item.getParent().getValue().getProject_name();
				Tooltip tooltip = new Tooltip(rootItem + "/" + item.getValue().getProject_name());
				tab1.setTooltip(tooltip);
				home_tabPane.getTabs().add(tab1);  // adding tab in tabview
				}
			}
			
		}
				
	});		
			
		
		
	}//end of initializable
	
	
	

	private String splitString(String project_name) {
		//spliting [ from string
		int index1=project_name.indexOf(">");
		int index2=project_name.indexOf("]");
		return project_name.substring(index1+1, index2);
	}
	
	 public void CreateButtonAction(MouseEvent e) {
		System.out.println("create button is clicked");
		//opening new stage for creation of stage
		FXMLLoader loader=new FXMLLoader(getClass().getResource("/application/ProjectCreation.fxml"));
		try {
			loader.load();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Parent p=loader.getRoot();
		Scene scene=new Scene(p);
		String css1=this.getClass().getResource("/application/application.css").toExternalForm();
		String css2=this.getClass().getResource("/application/ProjectCreation.css").toExternalForm();
		scene.getStylesheets().addAll(css1,css2);
		Stage stage =new Stage();
		Stage previous_stage=(Stage)((Node)e.getSource()).getScene().getWindow();
		stage.setScene(scene);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.initOwner(previous_stage);
		CurrentStage=stage;
		stage.show();
		
		
	}
	 
	 @SuppressWarnings("unchecked")
	public static void getProjectDetail() {
		 
		 root.getChildren().clear();
		 System.out.println("getproject details called");
			List<Project> data=ProjectDao.getProjectNameForTreeView(tmp.getVuser().getUser_id());
			
		 
		 String role_name;
		 for(Project element:data) {
				if(element.getRoleId()==1) {
					role_name="Manager";
				}else {
					role_name="Member";
				}
				TreeItem<ProjectModel> node=new TreeItem<>(new ProjectModel(element.getProjectId()+"",element.getprojectTitle()+"[-> "+role_name +"]"  )   );
				
				root.getChildren().add(node);
				TreeItem<ProjectModel> nodeA1 = new TreeItem<>(new ProjectModel("ProjectInformation", "Project Information"));
				TreeItem<ProjectModel> nodeB1 = new TreeItem<>(new ProjectModel("TeamMember", "Team Member"));
				TreeItem<ProjectModel> nodeC1 = new TreeItem<>(new ProjectModel("ProjectTask", "Project Task"));
				TreeItem<ProjectModel> nodeD1 = new TreeItem<>(new ProjectModel("TeamCalender", "Team Calender"));
				node.getChildren().addAll(nodeA1, nodeB1,nodeC1,nodeD1);
				
			}
		 
	 }
	 

}
