package com.info.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.info.utils.DbConnection;

public class TaskDao {

	private static Connection conn=null;
	private static PreparedStatement pst=null;
	private static ResultSet rs=null;
	
	public static List<String> getAllFileNameOfTaskThroughTaskId(int taskId){
		
		try {
			conn=DbConnection.getConnection();
			String query="SELECT task_filePath FROM taskfile WHERE task_id=?";
			pst=conn.prepareStatement(query);
			pst.setInt(1, taskId);
			rs=pst.executeQuery();
			List<String> fileList=new ArrayList<String>();
			while(rs.next()) {
				fileList.add(rs.getString("task_filePath"));
			}
			return fileList; 
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
		
		
		
		
	}
}
