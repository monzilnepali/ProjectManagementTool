package com.info.utils;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

	public static Connection getConnection(){
		try{
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/projectmanagement","root","");
		//	JOptionPane.showMessageDialog(null,"connection successful");
			//System.out.println("connection successfull");
			return con;
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
}
