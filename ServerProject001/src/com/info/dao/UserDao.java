package com.info.dao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.info.model.User;
import com.info.utils.DBConnection;

public class UserDao {

    
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
           
            while (rs.next()) {
             
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
}
