package com.server;

import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



public class SendMailHandler {


public static Boolean SendMailMethod(String msg,String ToAddress){
	  //Setting up configurations for the email connection to the Google SMTP server using TLS
    Properties props = new Properties();
    props.put("mail.smtp.host", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");//587 port for TLS and 465 for SSL
    props.put("mail.smtp.auth", "true");
    
    //Establishing a session with required user details
    Session session = Session.getInstance(props, new javax.mail.Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication("manjilnepali98@gmail.com", "Manjilgautam1");
        }
    });
    
    try {
        //Creating a Message object to set the email content
        MimeMessage msgMine = new MimeMessage(session);
        //Storing the comma seperated values to email addresses
       
        /*Parsing the String with defualt delimiter as a comma by marking the boolean as true and storing the email
        addresses in an array of InternetAddress objects*/
        InternetAddress[] address = InternetAddress.parse(ToAddress, true);
        //Setting the recepients from the address variable
        msgMine.setRecipients(Message.RecipientType.TO, address);
      //String timeStamp = new SimpleDateFormat("yyyymmdd_hh-mm-ss").format(new Date());
        msgMine.setSubject("Invitation");
        msgMine.setSentDate(new Date());
        msgMine.setText(msg);
        msgMine.setHeader("XPriority", "0");
        Transport.send(msgMine);
        System.out.println("Mail has been sent successfully");
        return true;
    } catch (MessagingException mex) {
        System.out.println("Unable to send an email" + mex);
    }
    return false;
    
    

     }



}
