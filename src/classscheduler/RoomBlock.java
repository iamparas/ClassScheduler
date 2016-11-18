package classscheduler;

public class RoomBlock extends Block{

	private String roomNumber;
	private String startTime;
	private String endTime;
	private int maxSize; 
	private boolean isReserved;
	
	public RoomBlock(String roomNumber, String startTime, String endTime, int maxSize){
		this.roomNumber = roomNumber;
		this.startTime = startTime;
		this.endTime = endTime;
		this.maxSize = maxSize;
		isReserved = false;
	}

	public boolean isReserved(){
		return isReserved;
	}
	
	public void setReservedStatus(boolean status){
		isReserved = status;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return String.format("%s (%s - %s)", roomNumber, startTime, endTime);
	}
	
	public int getMaxCapacity(){
		return maxSize;
	}
	
	
}
