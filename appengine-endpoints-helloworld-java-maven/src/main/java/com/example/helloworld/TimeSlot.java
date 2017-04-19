package com.example.helloworld;

import com.google.appengine.repackaged.org.joda.time.LocalTime;
import com.google.gson.annotations.Expose;

public class TimeSlot {
	LocalTime startTime;
	LocalTime endTime;

	public TimeSlot(int startHour, int startMin, int endHour, int endMin) {
		startTime = new LocalTime(startHour, startMin);
		endTime = new LocalTime(endHour, endMin);
	}
	
	public boolean equals(TimeSlot other){
	 return (startTime.equals(other.startTime) && endTime.equals(other.endTime));
	}
	
	
}