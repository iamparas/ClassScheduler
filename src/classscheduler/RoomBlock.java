package classscheduler;

public class RoomBlock extends Block{

	private String roomNumber;
	private String startTime;
	private String endTime;
	private int maxSize; 
	
	public RoomBlock(String roomNumber, String startTime, String endTime, int maxSize){
		this.roomNumber = roomNumber;
		this.startTime = startTime;
		this.endTime = endTime;
		this.maxSize = maxSize;
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
