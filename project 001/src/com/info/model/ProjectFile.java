package com.info.model;

public class ProjectFile {

    private int taskId;
    private String fileName;
    private String TeamMemberName;
    private String lastModified;
    private int projectId;
    
    
    public int getTaskId() {
        return taskId;
    }
    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getTeamMemberName() {
        return TeamMemberName;
    }
    public void setTeamMemberName(String teamMemberName) {
        TeamMemberName = teamMemberName;
    }
    public String getLastModified() {
        return lastModified;
    }
    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }
    public int getProjectId() {
        return projectId;
    }
    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }
    
}
