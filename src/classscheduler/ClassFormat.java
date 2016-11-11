package classscheduler;

public abstract class ClassFormat {
	public abstract int skip();
	public abstract int start();
	public abstract int numOfDays();
	public abstract int getMaxSpace(int blockSize);
}
