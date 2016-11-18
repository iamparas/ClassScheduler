package classscheduler;

public class Course {
	private String courseName;
	private String courseNumber;
	private int creditHour;
	private int maxAllowedEnrollment;
	private String assignedProfessor;
	
	public Course(String courseName, String courseNumber, int creditHour,int  maxAllowedEnrollment){
		this.courseName  = courseName;
		this.courseNumber = courseNumber;
		this.creditHour = creditHour;
		this.maxAllowedEnrollment = maxAllowedEnrollment;
	}
	
	public int getCreditHour(){
		return creditHour;
	}
	
	public int getMaxAllowedEnrollment(){
		return maxAllowedEnrollment;
	}
	
	public void setMaxAllowedEnrollment(int maxAllowedEnrollment){
		this.maxAllowedEnrollment = maxAllowedEnrollment;
	}
	
	public String getAssignedProfessor(){
		return assignedProfessor;
	}
	
	public void setAssignedProfessor(String professor){
		this.assignedProfessor = professor;
	}
	
	public String toString(){
		return String.format("%s", courseNumber);
	}
	
	
}
