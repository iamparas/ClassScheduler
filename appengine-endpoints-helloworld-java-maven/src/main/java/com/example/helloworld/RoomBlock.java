package com.example.helloworld;

public class RoomBlock extends Block{

	private Room room;
	private TimeSlot timeSlot;
	private boolean isReserved;
	
	public RoomBlock(Room room, TimeSlot timeSlot){
		this.room = room;
		this.timeSlot = timeSlot;
		isReserved = false;
	}

	public boolean isReserved(){
		return isReserved;
	}
	public TimeSlot getTimeSlot(){
		return timeSlot;
	}
	
	public int getMaxCapacity(){
		return room.roomSize;
	}
	
	public String getRoomID(){
		return room.getRoomID();
	}
	public void setReservedStatus(boolean status){
		isReserved = status;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String.format("%s (%s - %s)", room, timeSlot.startTime, timeSlot.endTime);
	}
	
	
	
	
}
