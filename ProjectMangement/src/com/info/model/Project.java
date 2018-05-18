package com.info.model;

import java.util.List;

public class Project {

	private int projectId;
	private String projectTitle;
	private String categories;
	private String projectDesc;
	private List<String> TeamMember;
	
	
	public int getProjectId() {
		return projectId;
	}
	public void setProjectId(int projectId) {
		this.projectId = projectId;
	}
	public String getprojectTitle() {
		return projectTitle;
	}
	public void setprojectTitle(String projectTitle) {
		this.projectTitle = projectTitle;
	}
	public String getProjectDesc() {
		return projectDesc;
	}
	public void setProjectDesc(String projectDesc) {
		this.projectDesc = projectDesc;
	}
	public List<String> getTeamMember() {
		return TeamMember;
	}
	public void setTeamMember(List<String> teamMember) {
		TeamMember = teamMember;
	}
	public String getCategories() {
		return categories;
	}
	public void setCategories(String categories) {
		this.categories = categories;
	}
	
	
}
