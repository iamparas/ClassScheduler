package classscheduler;

public class Course {
	private String courseName;
	private String courseNumber;
	private int creditHour;
	private int maxAllowedEnrollment;
	
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
	
	public String toString(){
		return String.format("%s", courseName);
	}
	
	
}
