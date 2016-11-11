package classscheduler;

public class MWF extends ClassFormat{
	private final int start = 0;
	private final int numOfDays = 3;
	private final int skip = 2;
	@Override
	public int skip() {
		return skip;
	}

	@Override
	public int start() {
		return start;
	}

	@Override
	public int numOfDays() {
		return numOfDays;
	}
	
	public int getMaxSpace(int blockSize){
		return numOfDays * blockSize;
	}
	
	
	
}
