package com.info.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.info.model.Project;
import com.info.utils.DBConnection;
import com.mysql.cj.protocol.Resultset;

public class ProjectDao {

		public static Boolean CreatProject(Project project ) {
				//add project data into database
				Connection conn=null;
				PreparedStatement pst=null;
				try {
					conn=DBConnection.getConnection();
					String query="INSERT INTO project (project_name,project_categories,project_desc,project_manager_id) VALUES (?,?,?,?)";
					pst=conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
					pst.setString(1, project.getprojectTitle());
					pst.setString(2, project.getCategories());
					pst.setString(3, project.getProjectDesc());
					pst.setInt(4, 10);
					pst.execute();
					
					//getting id of currently inserted data in project
					//project Id is assign to each team member of project
					ResultSet rs = pst.getGeneratedKeys();
					int generatedKey = 0;
					if (rs.next()) {
					    generatedKey = rs.getInt(1);
					}
					 
					System.out.println("Inserted record's ID: " + generatedKey);
					System.out.println("team size"+project.getTeamMember().size());
					//assigning project id to team member
					for(String email:project.getTeamMember()) {
						//gettting user id 
						//adding user_id and project id in userprojectTable
						System.out.println("team member email"+email);
						
						addTeamMember(generatedKey,GetUserId(email));
						
					}
					System.out.println("project created");
					
					
				}catch(Exception e) {
					e.printStackTrace();
				}finally {
					try {pst.close();} catch (SQLException e) {	e.printStackTrace();}
					try {conn.close();} catch (SQLException e) {	e.printStackTrace();}
					
				}
				
				return false;
			}
		
		private static int GetUserId(String email) {
			//gettting userId using email
			Connection conn=null;
			PreparedStatement pst=null;
			ResultSet rs=null;
			try {
				conn=DBConnection.getConnection();
				String query="SELECT user_id FROM user WHERE user_email=?";
				pst=conn.prepareStatement(query);
				pst.setString(1, email);
				rs=pst.executeQuery();
				while(rs.next()) {
					return rs.getInt("user_id");
				}
				
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				try {rs.close();} catch (SQLException e) {	e.printStackTrace();}
				try {pst.close();} catch (SQLException e) {	e.printStackTrace();}
				try {conn.close();} catch (SQLException e) {	e.printStackTrace();}
				
			}
			
			
			return 0;
			
		}
		private static void addTeamMember(int projectId,int userId) {
			Connection conn=null;
			PreparedStatement pst=null;
			try {
				conn=DBConnection.getConnection();
				String query="INSERT INTO userproject (project_id,user_id) VALUES (?,?)";
				pst=conn.prepareStatement(query);
				pst.setInt(1, projectId);
				pst.setInt(2, userId);
				pst.execute();
				
				
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		

}
