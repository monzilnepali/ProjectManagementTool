package com.info.dao;

import java.io.File;
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
import com.info.utils.DBConnection;

public class ProjectDao {
	private static Connection conn = null;
	private static PreparedStatement pst = null;
	private static ResultSet rs = null;

	public static int addNewTask(TaskModel newTask) {
		// creating new task
		conn = null;
		pst = null;
		try {
			conn = DBConnection.getConnection();
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
	public static String getEmailThroughId(int userid) {

		try {
			conn = DBConnection.getConnection();
			String query = "SELECT user_email FROM user WHERE	 user_id=?";
			pst = conn.prepareStatement(query);
			pst.setInt(1, userid);
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
			conn = DBConnection.getConnection();
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


	
	public static Boolean setProjectFile(int projectId, String filePath,
			String fileName, String fileSize) {
		try {
			conn = DBConnection.getConnection();
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
		ResultSet rs=null;
	   try {
		   conn=DBConnection.getConnection();
		   String query = "INSERT INTO project (project_name,project_categories,project_desc,project_creation) VALUES (?,?,?,?)";
			pst = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, newProject.getprojectTitle());
			pst.setString(2, newProject.getCategories());
			pst.setString(3, newProject.getProjectDesc());
			pst.setString(4, ProjectDao.getCurrentTime());

			pst.execute();
System.out.println("project created");
			// getting id of currently inserted data in project
			// project Id is assign to each team member of project
			 rs = pst.getGeneratedKeys();
			int generatedKey = 0;
			if (rs.next()) {
				generatedKey = rs.getInt(1);
			}
			return generatedKey;
		   
	   }catch(Exception e) {
		   e.printStackTrace();
	   }finally {
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
	public static void addTeamMember(int projectId, int userId, int roleId,int status) {
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
	
	public static int GetUserId(String email) {
		System.out.println("get user id called");
		// gettting userId using email
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
	
	public static List<String> getProjectFileThroughProjectId(int projectid){
		System.out.println("getProjectFileThroughProjectId called  -->"+projectid);
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn=DBConnection.getConnection();
			String query="SELECT project_filePath from projectfile WHERE project_id=?";
			pst=conn.prepareStatement(query);
			pst.setInt(1, projectid);
			rs=pst.executeQuery();
			List<String> filePathList=new ArrayList<>();
			while(rs.next()) {
				filePathList.add( rs.getString("project_filePath"));
			}
			
			return filePathList;
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
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
	
	public static String getProjectNameThroughId(int id) {
		
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
				
				return rs.getString("project_name");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}finally {
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
	public static void setCompletedTaskFile(int task_id,String filePath,String filename,String filesize) {
		
		
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
		conn=DBConnection.getConnection();
		String query="INSERT INTO taskcompletefile VALUES(?,?,?,?)";
		pst=conn.prepareStatement(query);
		pst.setInt(1, task_id);
	   pst.setString(2, filePath);
	   pst.setString(3, filename);
	   pst.setString(4, filesize);
	   pst.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			
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
	
	public static void setTaskStatus(int taskid,String status) {
		
		Connection conn = null;
		PreparedStatement pst = null;
		
		try {
		conn=DBConnection.getConnection();
		String query="UPDATE task SET task_status=? where task_id=?";
		pst=conn.prepareStatement(query);
		pst.setString(1, status);
		pst.setInt(2, taskid);
		pst.execute();
		

		} catch (Exception e) {
			e.printStackTrace();
		}finally {
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
	public static String getEmailAddressOfProjectManage(int projectid) {
		
		Connection conn=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		try{
			conn=DBConnection.getConnection();
			String query="SELECT user_email FROM user  INNER JOIN userproject ON user.user_id=userproject.user_id   where project_id=? AND role_id=? ";
			pst=conn.prepareStatement(query);
			pst.setInt(1, projectid);
			pst.setInt(2, 1);//1= for manager and 2 for team memeber
			rs=pst.executeQuery();
			while(rs.next()) {
				//return email of manager
				return rs.getString("user_email");
			}
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
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
	
	public static List<String> getTaskInformation(int taskId) {
		
		Connection conn=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		try{
			conn=DBConnection.getConnection();
			String query="SELECT task_name,user_name,task_desc,task_deadline,task_assignDate,project_name FROM "
					+ " task INNER JOIN user on task.user_id=user.user_id "
					+ "INNER JOIN project on task.project_id=project.project_id WHERE task_id=? ";
			pst=conn.prepareStatement(query);
			pst.setInt(1, taskId);
			rs=pst.executeQuery();
			ArrayList<String> taskInfo=new ArrayList<String>();
			while(rs.next()) {
				
				
				taskInfo.add(rs.getString("task_name"));
				taskInfo.add(rs.getString("user_name"));
				taskInfo.add(rs.getString("task_desc"));
				taskInfo.add(rs.getString("task_deadline"));
				taskInfo.add(rs.getString("task_assignDate"));
				taskInfo.add(rs.getString("project_name"));
				return taskInfo;

			}
			
					
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
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
	public static String getUserNameThroughId(int userid) {
		
		Connection conn=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		try{
			conn=DBConnection.getConnection();
		    String query="SELECT user_name FROM user WHERE user_id=?";
		    pst=conn.prepareStatement(query);
		    pst.setInt(1, userid);
		    rs=pst.executeQuery();
		    while(rs.next()) {
		    	return rs.getString("user_name");
		    }
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
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
	public static int getUserIdFromtask(int taskId) {
		
		
		Connection conn=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		try{
			conn=DBConnection.getConnection();
		    String query="SELECT user_id FROM task WHERE task_id=?";
		    pst=conn.prepareStatement(query);
		    pst.setInt(1, taskId);
		    rs=pst.executeQuery();
		    while(rs.next()) {
		    	return rs.getInt("user_id");
		    }
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
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

	public static List<Project> getProjectNameViaUserId(int userId) {
	    System.out.println("getprokectName via user id called,user id= "+userId);
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
                System.out.println("fetch details of project"+rs.getString("project_name"));
                Project pro = new Project();
                pro.setProjectId(rs.getInt("project_id"));
                pro.setprojectTitle(rs.getString("project_name"));
                pro.setRoleId(rs.getInt("role_id"));
                System.out.println("current user role is "+rs.getInt("role_id"));
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


}
