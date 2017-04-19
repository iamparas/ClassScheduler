package com.example.helloworld;

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
		int start = f.start(), skip=f.skip(), numOfDays=f.numOfDays();
		
		for(start = 0; start < WEEKDAYS; start = start + skip){
			double eachDay = c.getCreditHour() * 1.0/ numOfDays;
			int i = 0;
			
			while(eachDay > 0.0){
//				blocks[i][start] = c;
				eachDay -= 1.0;
				capLeft -= 1.0;
				i++;
			}
		}
	}
}
