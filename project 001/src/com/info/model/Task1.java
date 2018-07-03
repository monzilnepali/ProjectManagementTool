package com.info.model;

public class Task1 {
	private int id;
	private String name;
	
	
	public Task1(int id,String name) {
		this.id=id;
		this.name=name;
	}
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	@Override
	public String toString(){
	    return name;
	}
}
