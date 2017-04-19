package com.example.helloworld;
import java.util.ArrayList;
import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class Course {
	
	 private String courseName;
	
	@Id
	private String courseNumber;
	
	private int creditHour;
	private int maxAllowedEnrollment;
	private String assignedProfessor;
	private List<TimeSlot> preferredSlots;

	private Course(){}
	public Course(String courseName, String courseNumber, 
				  int creditHour, int maxAllowedEnrollment,
				  List<TimeSlot> preferredSlots) {
		this.courseName = courseName;
		this.courseNumber = courseNumber;
		this.creditHour = creditHour;
		this.maxAllowedEnrollment = maxAllowedEnrollment;
		this.preferredSlots = preferredSlots;
		if(preferredSlots == null){
			preferredSlots = new ArrayList<TimeSlot>();
		}
	}

	public Course(String courseName, String courseNumber, 
			  int creditHour, int maxAllowedEnrollment) {
		this(courseName, courseNumber, creditHour, maxAllowedEnrollment,
				new ArrayList<TimeSlot>());
	}
	public String getCourseName(){
		return courseName;
	}
	
	public String getCourseNumber(){
		return courseNumber;
	}
	public int getCreditHour() {
		return creditHour;
	}

	public int getMaxAllowedEnrollment() {
		return maxAllowedEnrollment;
	}

	public void setPreference(int startHour, int startMinute, int endHour, int endMinute) {
		preferredSlots.add(new TimeSlot(startHour, startMinute, endHour, endMinute));
	}
	public List<TimeSlot> getPreferences(){
		return preferredSlots;
	}
	public void setMaxAllowedEnrollment(int maxAllowedEnrollment) {
		this.maxAllowedEnrollment = maxAllowedEnrollment;
	}

	public String getAssignedProfessor() {
		return assignedProfessor;
	}

	public void setAssignedProfessor(String professor) {
		this.assignedProfessor = professor;
	}

//	public Key<Course> getKey(){
//		return Key.create(Course.class, courseNumber);
//	}
	public String toString() {
		return String.format("%s", courseNumber);
	}
	
	
}
