package classscheduler;

import java.util.List;

public class OneHourBlock {
	Course[] blocks; 
	
	private final int BLOCKSIZE = 1;
	private final int WEEKDAYS = 5;
	private int capLeft; 
	ClassFormat classFormat;
	
	public OneHourBlock(){
		blocks = new Course[WEEKDAYS];
		capLeft = BLOCKSIZE * WEEKDAYS;
	}
	
	
	public void addCourse(Course c, ClassFormat f){
		this.classFormat = f;
		int start, skip, numOfDays;
		if(f == ClassFormat.MWF){
			start = 0;
			skip = 2;
			numOfDays = 3;
		}else if(f == ClassFormat.TTH){
			start = 1;
			skip = 2;
			numOfDays = 3;
		}else{
			start = 0;
			skip = 1;
			numOfDays = 7;
		}
		
		for(start = 0; start < WEEKDAYS; start = start + skip){
			double eachDay = c.getCreditHour() * 1.0/ numOfDays;
			int i = 0;
			
			while(eachDay > 0.0){
				blocks[i][start] = c;
				eachDay -= 1.0;
				capLeft -= 1.0;
				i++;
			}
		}
	}
}
