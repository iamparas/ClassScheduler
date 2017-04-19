package com.example.helloworld;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class PreferredTimeFeature implements Feature{
	private static final Logger LOG = Logger.getLogger(PreferredTimeFeature.class.getName());
	public static double WEIGHT;
	List<TimeSlot> preferredTime;
	TimeSlot timeSlot;
	public PreferredTimeFeature(List<TimeSlot> preferredTime, TimeSlot timeSlot){
		this.preferredTime = preferredTime;
		this.timeSlot = timeSlot;
	}

	public double getWeight() {
		return PreferredTimeFeature.WEIGHT;
	}

	public double getValue() {
	    int MAX_VAL = 100;
	    LOG.warning(timeSlot.toString());
	    if(preferredTime == null){
	    	 return 0;
	    }
	   
		for(int i = 0; i < preferredTime.size(); i++){
			
			if(preferredTime.get(i).equals(timeSlot)){
				int subtractor = i  * MAX_VAL / preferredTime.size();
				return  MAX_VAL  - subtractor;
			}
		}
		return 0;
	}
}
