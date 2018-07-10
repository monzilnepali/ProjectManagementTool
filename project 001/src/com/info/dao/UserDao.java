package com.info.dao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.info.model.Project;
import com.info.model.User;
import com.info.utils.DBConnection;

public class UserDao {

	public static Boolean emailValidator(String email) {
		// for checking the pattern of email

		String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();// match =true return
	}// end email validator

	public static Boolean usernameCheck(String username) {
		// to check whether user already exist or not
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DBConnection.getConnection();
			String query = "SELECT user_name FROM user WHERE user_name=?";
			pst = conn.prepareStatement(query);
			pst.setString(1, username);
			rs = pst.executeQuery();
			while (rs.next()) {
				return true;// mean username already exist,show error username already exist
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
			} catch (Exception e) {
				/* nothing */}
			try {
				pst.close();
			} catch (Exception e) {
				/* nothing */}
			try {
				conn.close();
			} catch (Exception e) {
				/* nothing */}
		}

		return false;// means username doesnot exist
	}// end of username check

	public static User userRegister(User user) {
		Connection conn = null;
		PreparedStatement pst = null;
		try {
			conn = DBConnection.getConnection();
			String query = "INSERT into user (user_email,user_name,user_password) VALUES (?,?,?)";
			pst = conn.prepareStatement(query);
			pst.setString(1, user.getUser_email());
			pst.setString(2, user.getUser_name());
			pst.setString(3, hashing(user.getUser_password()));
			pst.execute();
			user.setUser_password("");
			return user;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				pst.close();
			} catch (Exception e) {
				/* nothing */}
			try {
				conn.close();
			} catch (Exception e) {
				/* nothing */}
		}

		return null;
	}// end of userRegister

	public static User userAuth(User user) {
		// checking user emailaddress and password
		System.out.println("user auth called" + user.getUser_email() + user.getUser_password());
		Connection conn = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			conn = DBConnection.getConnection();
			String query = "SELECT * FROM user where user_email=? AND user_password=?";
			pst = conn.prepareStatement(query);
			pst.setString(1, user.getUser_email());
			pst.setString(2, hashing(user.getUser_password()));
			rs = pst.executeQuery();
			System.out.println("1");
			while (rs.next()) {
				System.out.println("2");
				user.setUser_id(rs.getInt("user_id"));
				user.setUser_name(rs.getString("user_name"));
				user.setUser_password(null);
				
				return user;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				pst.close();
			} catch (Exception e) {
				/* nothing */}
			try {
				conn.close();
			} catch (Exception e) {
				/* nothing */}
		}

		return null;

	}
	
	
	
	public static final String hashing(String password) throws NoSuchAlgorithmException, NoSuchProviderException {
		// for hashing user password before storing in database using SHA1 algorithm
		String hpassword = password;
		MessageDigest mDigest = MessageDigest.getInstance("SHA1");
		byte[] result = mDigest.digest(hpassword.getBytes());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < result.length; i++) {
			sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
		}
		return sb.toString();
	}// end of hashing function
	
	
	public static String formattedDate(Date date) {
		//formatting the date data.utils
		
		SimpleDateFormat ft =new SimpleDateFormat ("E yyyy-MM-dd 'at' hh:mm a");
		return ft.format(date);
	}

}
