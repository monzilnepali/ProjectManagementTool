package com.info.controller;

import com.info.model.User;

public class CurrentUserSingleton {

	private User vuser;
	private static  CurrentUserSingleton singleton=new CurrentUserSingleton();
	
	/* A private Constructor prevents any other
	    * class from instantiating.
	    */
	private CurrentUserSingleton() {}
	public static CurrentUserSingleton getInstance() {
		return singleton;
	}
	
	protected User getVuser() {
		return vuser;
	}
	protected void setVuser(User vuser) {
		this.vuser = vuser;
	}
}
