package com.example.helloworld;

import java.util.ArrayList;
import java.util.List;

public class Instructor {

	private String name;
	private String position;
	public List<Course> courses;
	public Instructor(String name, String position){
		this.name = name;
		this.position = position;
		courses = new ArrayList<Course>();
	}
	
	public void addCourse(Course course){
		courses.add(course);
	}
	
	public List<Course> getCourses(){
		return courses;
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
