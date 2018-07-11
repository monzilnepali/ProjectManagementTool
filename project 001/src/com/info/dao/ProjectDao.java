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

import com.info.controller.CurrentUserSingleton;
import com.info.model.Project;
import com.info.model.TaskModel;
import com.info.model.User;
import com.info.utils.DBConnection;

import javafx.util.Callback;

public class ProjectDao {

	public static Boolean CreatProject(Project project, int managerId) {

		// add project data into database
		Connection conn = null;
		PreparedStatement pst = null;
		try {
			conn = DBConnection.getConnection();
			String query = "INSERT INTO project (project_name,project_categories,project_desc,project_creation) VALUES (?,?,?,?)";
			pst = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, project.getprojectTitle());
			pst.setString(2, project.getCategories());
			pst.setString(3, project.getProjectDesc());
			pst.setString(4, ProjectDao.getCurrentTime());

			pst.execute();

			// getting id of currently inserted data in project
			// project Id is assign to each team member of project
			ResultSet rs = pst.getGeneratedKeys();
			int generatedKey = 0;
			if (rs.next()) {
				generatedKey = rs.getInt(1);
			}

			// System.out.println("Inserted record's ID: " + generatedKey);
			addTeamMember(generatedKey, managerId, 1, 1);
			// assigning project id to team member
			for (String email : project.getTeamMember()) {
				// gettting user id
				// adding user_id and project id in userprojectTable
				System.out.println("team member email" + email);

				addTeamMember(generatedKey, GetUserId(email), 2, 0);

			}
			System.out.println("<project created>");
			return true;

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

		return false;
	}

	private static int GetUserId(String email) {
		// gettting userId using email from database
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DBConnection.getConnection();
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
	private static void addTeamMember(int projectId, int userId, int roleId,
			int status) {
		// assign project id and useid respectively
		Connection conn = null;
		PreparedStatement pst = null;
		try {
			conn = DBConnection.getConnection();
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

	public static List<Project> getProjectNameForTreeView(int userId) {
		// getting name of project of currently logged in user
		// System.out.println("getprojectname called");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DBConnection.getConnection();
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

	public static Project getProjectInformation(int project_id) {

		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DBConnection.getConnection();
			String query = "SELECT * FROM project WHERE project_id=?";
			pst = conn.prepareStatement(query);
			pst.setInt(1, project_id);

			rs = pst.executeQuery();
			while (rs.next()) {
				Project pro = new Project();
				pro.setprojectTitle(rs.getString("project_name"));
				pro.setProjectDesc(rs.getString("project_desc"));
				pro.setCategories(rs.getString("project_categories"));
				pro.setProjectCreationDate(rs.getString("project_creation"));
				return pro;
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
		return null;

	}
	public static List<User> getTeamMemberNameOfProject(int projectId) {
		System.out.println("gettema member name called");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DBConnection.getConnection();
			String query = "SELECT user.user_id,user.user_name FROM userproject INNER JOIN user ON userproject.user_id=user.user_id WHERE userproject.project_id=? AND userproject.role_id=?";
			pst = conn.prepareStatement(query);
			pst.setInt(1, projectId);
			pst.setInt(2, 2);
			rs = pst.executeQuery();
			List<User> userlist = new ArrayList<>();
			while (rs.next()) {
				User user = new User();
				user.setUser_id(rs.getInt("user_id"));
				user.setUser_name(rs.getString("user_name"));
				userlist.add(user);

			}
			return userlist;

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

		return null;

	}
	public static Boolean addNewTask(TaskModel newTask) {

		Connection conn = null;
		PreparedStatement pst = null;
		try {
			conn = DBConnection.getConnection();
			String query = "INSERT INTO task (task_name,user_id,task_deadline,task_priority,project_id) VALUES (?,?,?,?,?)";
			pst = conn.prepareStatement(query);
			pst.setString(1, newTask.getTaskName());
			pst.setInt(2, newTask.getTaskAssignTo());
			pst.setString(3, newTask.getTaskDeadLine());
			pst.setString(4, newTask.getTaskPriority());
			pst.setInt(5, newTask.getProjectId());
			pst.execute();
			System.out.println("new task added");
			return true;

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
		return false;
	}

	public static String getCurrentTime() {
		// getting currentTime
		Date CurrentDate = new Date();

		SimpleDateFormat ft = new SimpleDateFormat("E yyyy-MM-dd 'at' hh:mm");
		return ft.format(CurrentDate);

	}

	public static List<TaskModel> getTaskList(int projectId, String role,
			int userId) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DBConnection.getConnection();
			String query = null;
			if (role.equals(" Manager")) {

				query = "SELECT  task_id,task_name,user_name,task_deadline,task_priority,task_status,project_id,task_assignDate,task_desc FROM task INNER JOIN user ON task.user_id=user.user_id WHERE project_id=?";
				pst = conn.prepareStatement(query);
				pst.setInt(1, projectId);
			} else {
				query = "SELECT  task_id,task_name,user_name,task_deadline,task_priority,task_status,project_id,task_assignDate,task_desc FROM task INNER JOIN user ON task.user_id=user.user_id WHERE project_id=? AND user.user_id=?";
				pst = conn.prepareStatement(query);
				pst.setInt(1, projectId);
				pst.setInt(2, userId);
			}

			rs = pst.executeQuery();
			List<TaskModel> taskList = new ArrayList<>();
			while (rs.next()) {
				TaskModel newTask = new TaskModel();
				newTask.setTaskId(rs.getInt("task_id"));
				newTask.setTaskName(rs.getString("task_name"));
				newTask.setProjectId(rs.getInt("project_id"));
				newTask.setTaskAssignToName(rs.getString("user_name"));
				newTask.setTaskDeadLine(rs.getString("task_deadline"));
				newTask.setTaskPriority(rs.getString("task_priority"));
				newTask.setTaskStatus(rs.getString("task_status"));
				newTask.setTaskCreationDate(rs.getString("task_assignDate"));
				newTask.setTaskDescription(rs.getString("task_desc"));
				taskList.add(newTask);

			}
			return taskList;

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

	
	
	public static List<TaskModel> getCompletedTaskList(int projectId, String role,
			int userId,String taskstatus) {
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DBConnection.getConnection();
			String query = null;
			if (role.equals(" Manager")) {

				query = "SELECT  task_id,task_name,user_name,task_deadline,task_priority,task_status,project_id,task_assignDate,task_desc FROM task INNER JOIN user ON task.user_id=user.user_id WHERE project_id=? AND task_status=?";
				pst = conn.prepareStatement(query);
				pst.setInt(1, projectId);
				pst.setString(2, taskstatus);
			} else {
				query = "SELECT  task_id,task_name,user_name,task_deadline,task_priority,task_status,project_id,task_assignDate,task_desc FROM task INNER JOIN user ON task.user_id=user.user_id WHERE project_id=? AND user.user_id=? AND task_status=?";
				pst = conn.prepareStatement(query);
				pst.setInt(1, projectId);
				pst.setInt(2, userId);
				pst.setString(3, taskstatus);
			}

			rs = pst.executeQuery();
			List<TaskModel> taskList = new ArrayList<>();
			while (rs.next()) {
				TaskModel newTask = new TaskModel();
				newTask.setTaskId(rs.getInt("task_id"));
				newTask.setTaskName(rs.getString("task_name"));
				newTask.setProjectId(rs.getInt("project_id"));
				newTask.setTaskAssignToName(rs.getString("user_name"));
				newTask.setTaskDeadLine(rs.getString("task_deadline"));
				newTask.setTaskPriority(rs.getString("task_priority"));
				newTask.setTaskStatus(rs.getString("task_status"));
				newTask.setTaskCreationDate(rs.getString("task_assignDate"));
				newTask.setTaskDescription(rs.getString("task_desc"));
				taskList.add(newTask);

			}
			return taskList;

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
	
	public static Boolean updateTaskStatus(int taskId, String taskStatus) {
		System.out.println("update task status called");
		Connection con = null;
		PreparedStatement pst = null;
		try {
			con = DBConnection.getConnection();
			String query = "UPDATE task SET task_status=? WHERE task_id=?";
			pst = con.prepareStatement(query);
			pst.setString(1, taskStatus);
			pst.setInt(2, taskId);
			pst.execute();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}
	public static String getProjectNameThroughId(int id) {
		System.out.println("getprojetName through id called");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DBConnection.getConnection();
			String query = "SELECT project_name FROM project WHERE project_id=?";
			pst = conn.prepareStatement(query);
			pst.setInt(1, id);
			rs = pst.executeQuery();
			while (rs.next()) {
				System.out.println(
						"the project name" + rs.getString("project_name"));
				return rs.getString("project_name");
			}

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

	public static List<String> getTaskFileThroughId(int taskId) {
		System.out.println("gettask tile through id");
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DBConnection.getConnection();
			String query = "SELECT file_name,file_size FROM taskfile WHERE task_id=?";
			pst = conn.prepareStatement(query);
			pst.setInt(1, taskId);
			rs = pst.executeQuery();

			List<String> list = new ArrayList<>();
			while (rs.next()) {
				System.out.println(rs.getString("file_name") + "\t"
						+ rs.getString("file_size"));
				list.add(rs.getString("file_name") + "\t"
						+ rs.getString("file_size"));
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
	
	public static List<String> getProjectFileThroughProjectId(int projectid){
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
		   conn=DBConnection.getConnection();
		   String query="SELECT project_fileName,project_fileSize FROM projectfile 	WHERE project_id=?";
		   pst=conn.prepareStatement(query);
		   pst.setInt(1, projectid);
		   rs=pst.executeQuery();
		   List<String> fileList=new ArrayList<>();
		   while(rs.next()) {
			   
			   fileList.add(rs.getString("project_fileName")+"\t"+rs.getString("project_fileSize"));
		   }
		   return fileList;
		}catch(Exception e) {
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

}
