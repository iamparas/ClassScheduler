package classscheduler;

public class Course {
	private String courseName;
	private String courseNumber;
	private int creditHour;
	
	public Course(String courseName, String courseNumber, int creditHour){
		this.courseName  = courseName;
		this.courseNumber = courseNumber;
		this.creditHour = creditHour;
	}
	
	public int getCreditHour(){
		return creditHour;
	}
	
	public String toString(){
		return String.format("%s", courseName);
	}
	
	
}
