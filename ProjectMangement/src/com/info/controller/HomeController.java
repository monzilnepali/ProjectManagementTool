package com.info.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.info.model.ProjectModel;
import com.jfoenix.controls.JFXTabPane;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class HomeController implements Initializable {
	@FXML
	private TreeView<ProjectModel> projectTree;
	@FXML
	private JFXTabPane home_tabPane;
	private static Boolean flag=false;

	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		SingleSelectionModel<Tab> selectionModel = home_tabPane.getSelectionModel();
		// TODO Auto-generated method stub
		System.out.println("home controlle called");

		TreeItem<ProjectModel> root = new TreeItem<>(new ProjectModel("", "Project"));
		root.setExpanded(true);

		TreeItem<ProjectModel> nodeA = new TreeItem<>(new ProjectModel("", "Password Manager"));
		TreeItem<ProjectModel> nodeB = new TreeItem<>(new ProjectModel("", "banking Application"));
		root.getChildren().addAll(nodeA, nodeB);

		TreeItem<ProjectModel> nodeA1 = new TreeItem<>(new ProjectModel("ProjectInformation", "Project Information"));
		TreeItem<ProjectModel> nodeB1 = new TreeItem<>(new ProjectModel("TeamMember", "Team Member"));
		nodeA.getChildren().addAll(nodeA1, nodeB1);

		TreeItem<ProjectModel> nodeA2 = new TreeItem<>(new ProjectModel("ProjectInformation", "Project Information"));
		TreeItem<ProjectModel> nodeB2 = new TreeItem<>(new ProjectModel("TeamMember", "Team Member"));
		nodeB.getChildren().addAll(nodeA2, nodeB2);

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
				
				
					
		
		

	}//end of initializable

	public void homeDataInitial() {
		// System.out.println("home controller method called");
	}

}
