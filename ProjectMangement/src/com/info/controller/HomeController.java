package com.info.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.info.dao.ProjectDao;
import com.info.model.ProjectModel;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTreeView;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HomeController implements Initializable {
	@FXML
	private JFXTreeView<ProjectModel> projectTree;
	@FXML
	private JFXTabPane home_tabPane;
	private static Boolean flag=false;
     private static TreeItem<ProjectModel> root;
     protected static ObservableList<String> list;
     protected static int VerifiedUser;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	}//end of initializable

	public void homeDataInitial(int CurrentUserId) {
		// System.out.println("home controller method called");
		HomeController.VerifiedUser=CurrentUserId;
		System.out.println("verified user "+CurrentUserId);
		
		
		
			SingleSelectionModel<Tab> selectionModel = home_tabPane.getSelectionModel();
		
		System.out.println("home controlle called");
		//tree view root element which is only one
	   root = new TreeItem<>(new ProjectModel("", "Project"));
		root.setExpanded(true);
		
		getProjectDetail();



		projectTree.setRoot(root);

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
						System.out.println("parent" + a.getId() + "name" + a.getText());
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
					
					try {
						Parent p = FXMLLoader.load(
								getClass().getResource("/application/" + item.getValue().getproject_file() + ".fxml"));
						tab1.setContent(p);
					} catch (IOException e1) {

						e1.printStackTrace();
					}
					String rootItem = item.getParent().getValue().getProject_name();
					Tooltip tooltip = new Tooltip(rootItem + "/" + item.getValue().getProject_name());
					tab1.setTooltip(tooltip);
					home_tabPane.getTabs().add(tab1);  // adding tab in tabview
					}
				}
				
			}
					
		});		
				
				
					
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
		stage.show();
		
		
	}
	 
	 @SuppressWarnings("unchecked")
	public static void getProjectDetail() {
		 
		 root.getChildren().clear();
		 System.out.println("getproject details called");
			List<String> data=ProjectDao.getProjectNameForTreeView(VerifiedUser);
			
		 
		 
		 for(String element:data) {
				
				TreeItem<ProjectModel> node=new TreeItem<>(new ProjectModel("",element));
				root.getChildren().add(node);
				TreeItem<ProjectModel> nodeA1 = new TreeItem<>(new ProjectModel("ProjectInformation", "Project Information"));
				TreeItem<ProjectModel> nodeB1 = new TreeItem<>(new ProjectModel("TeamMember", "Team Member"));
				node.getChildren().addAll(nodeA1, nodeB1);
				
			}
		 
	 }
	 

}
