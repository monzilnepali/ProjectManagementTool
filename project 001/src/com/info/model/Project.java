package com.info.model;

import java.io.File;
import java.io.Serializable;
import java.util.List;

@SuppressWarnings("serial")
public class Project implements Serializable {

	private int projectId;
	private String projectTitle;
	private String categories;
	private String projectDesc;
	private List<String> TeamMember;
	private String projectCreationDate;
	private List<File> fileList;
	private String projectImage;
	private File projectImageFile;
	private int roleId;
	
	
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
	public String getProjectCreationDate() {
		return projectCreationDate;
	}
	public void setProjectCreationDate(String projectCreationDate) {
		this.projectCreationDate = projectCreationDate;
	}
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	public List<File> getFileList() {
		return fileList;
	}
	public void setFileList(List<File> fileList) {
		this.fileList = fileList;
	}
    public String getProjectImage() {
        return projectImage;
    }
    public void setProjectImage(String projectImage) {
        this.projectImage = projectImage;
    }
    public File getProjectImageFile() {
        return projectImageFile;
    }
    public void setProjectImageFile(File projectImageFile) {
        this.projectImageFile = projectImageFile;
    }
	
	
}
