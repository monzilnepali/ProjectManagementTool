package com.info.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.info.model.Project;
import com.info.model.TaskModel;

import com.info.utils.DbConnection;

public class ProjectDao {
	private static Connection conn = null;
	private static PreparedStatement pst = null;
	private static ResultSet rs = null;

	public static int addNewTask(TaskModel newTask) {
		// creating new task
		conn = null;
		pst = null;
		try {
			conn = DbConnection.getConnection();
			String query = "INSERT INTO task (task_name,user_id,task_deadline,task_priority,project_id,task_assignDate) VALUES (?,?,?,?,?,?)";
			pst = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, newTask.getTaskName());
			pst.setInt(2, newTask.getTaskAssignTo());
			pst.setString(3, newTask.getTaskDeadLine());
			pst.setString(4, newTask.getTaskPriority());
			pst.setInt(5, newTask.getProjectId());
			pst.setString(6, newTask.getTaskCreationDate());
			pst.execute();

			ResultSet rs = pst.getGeneratedKeys();
			int generatedKey = 0;
			if (rs.next()) {
				generatedKey = rs.getInt(1);
			}

			System.out.println("new task added");
			return generatedKey;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				/*  */}
			try {
				conn.close();
			} catch (SQLException e) {
				/*  */}

		}
		return 0;
	}
	public static String getEmailThroughId(int userId) {

		try {
			conn = DbConnection.getConnection();
			String query = "SELECT user_email FROM user WHERE	 user_id=?";
			pst = conn.prepareStatement(query);
			pst.setInt(1, userId);
			rs = pst.executeQuery();
			while (rs.next()) {
				return rs.getString("user_email");
			}

		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				/*  */}
			try {
				pst.close();
			} catch (SQLException e) {
				/*  */}
			try {
				conn.close();
			} catch (SQLException e) {
				/*  */}

		}

		return null;

	}
	public static Boolean setTaskFile(int taskId, String filePath,
			String fileName, String fileSize) {
		try {
			conn = DbConnection.getConnection();
			String query = "INSERT INTO taskFile VAlUES(?,?,?,?)";
			pst = conn.prepareStatement(query);
			pst.setInt(1, taskId);

			pst.setString(2, filePath);
			pst.setString(3, fileName);
			pst.setString(4, fileSize);
			pst.execute();
			return true;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				/*  */}
			try {
				conn.close();
			} catch (SQLException e) {
				/*  */}

		}
		return false;

	}


	public static List<Project> getProjectNameForTreeView(int userId) {
		// getting name of project of currently logged in user
		// System.out.println("getprojectname called");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DbConnection.getConnection();
			String query = "SELECT project.project_name,project.project_id,userproject.role_id FROM project INNER JOIN userproject ON project.project_id=userproject.project_Id WHERE userproject.user_id=?";
			pst = conn.prepareStatement(query);
			pst.setInt(1, userId);

			rs = pst.executeQuery();
			List<Project> list = new ArrayList<Project>();
			while (rs.next()) {
				// System.out.println("list new data");

				Project pro = new Project();
				pro.setProjectId(rs.getInt("project_id"));
				pro.setprojectTitle(rs.getString("project_name"));
				pro.setRoleId(rs.getInt("role_id"));
				list.add(pro);
			}
			return list;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
		return null;
	}

	
	public static Boolean setProjectFile(int projectId, String filePath,
			String fileName, String fileSize) {
		try {
			conn = DbConnection.getConnection();
			String query = "INSERT INTO projectfile VAlUES(?,?,?,?)";
			pst = conn.prepareStatement(query);
			pst.setInt(1, projectId);

			pst.setString(2, filePath);
			pst.setString(3, fileName);
			pst.setString(4, fileSize);
			pst.execute();
			return true;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				/*  */}
			try {
				conn.close();
			} catch (SQLException e) {
				/*  */}

		}
		return false;

	}
	
	
	
	public static String getCurrentTime() {
		// getting currentTime
		Date CurrentDate = new Date();

		SimpleDateFormat ft = new SimpleDateFormat("E yyyy-MM-dd 'at' hh:mm");
		return ft.format(CurrentDate);

	}
	public static int CreatProject(Project newProject, int managerId) {
	   try {
		   conn=DbConnection.getConnection();
		   String query = "INSERT INTO project (project_name,project_categories,project_desc,project_creation) VALUES (?,?,?,?)";
			pst = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, newProject.getprojectTitle());
			pst.setString(2, newProject.getCategories());
			pst.setString(3, newProject.getProjectDesc());
			pst.setString(4, ProjectDao.getCurrentTime());

			pst.execute();

			// getting id of currently inserted data in project
			// project Id is assign to each team member of project
			ResultSet rs = pst.getGeneratedKeys();
			int generatedKey = 0;
			if (rs.next()) {
				generatedKey = rs.getInt(1);
			}
			return generatedKey;
		   
	   }catch(Exception e) {
		   e.printStackTrace();
	   }
		
		
		
		return 0;
	}
	public static void addTeamMember(int projectId, int userId, int roleId,int status) {
		// assign project id and useid respectively
		Connection conn = null;
		PreparedStatement pst = null;
		try {
			conn = DbConnection.getConnection();
			String query = "INSERT INTO userproject (project_id,user_id,role_id,status) VALUES (?,?,?,?)";
			pst = conn.prepareStatement(query);
			pst.setInt(1, projectId);
			pst.setInt(2, userId);
			pst.setInt(3, roleId);
			pst.setInt(4, status);
			pst.execute();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}
	
	public static int GetUserId(String email) {
		System.out.println("get user id called");
		// gettting userId using email
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DbConnection.getConnection();
			String query = "SELECT user_id FROM user WHERE user_email=?";
			pst = conn.prepareStatement(query);
			pst.setString(1, email);
			rs = pst.executeQuery();
			while (rs.next()) {
				return rs.getInt("user_id");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				pst.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return 0;

	}


}
