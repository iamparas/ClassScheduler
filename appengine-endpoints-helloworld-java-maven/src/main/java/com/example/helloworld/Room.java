package com.example.helloworld;

import java.util.HashMap;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class Room {
	@Id
	private String roomID;
	 String roomNumber;
	 String buildingName;
	 int roomSize; 
	
	private Room(){}
	
	public Room(String roomNumber, String buildingName, int roomSize){
		this.roomNumber = roomNumber;
		this.buildingName = buildingName;
		this.roomSize = roomSize;
		this.roomID = String.format("%s%s", getAbbreviation(), roomNumber);
	}
	private String getAbbreviation(){
		String[] words = buildingName.split(" ");
		String abbrev="";
		for(String s : words){
			abbrev += s.substring(0, 1);
		}
		return abbrev;
	}
	
	public String getRoomID(){
		return roomID;
	}
	public String toString(){
		return String.format("%s %s", this.roomNumber, this.buildingName);
	}
	
	
	
	
}
