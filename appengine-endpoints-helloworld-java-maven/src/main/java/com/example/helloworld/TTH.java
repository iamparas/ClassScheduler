package com.example.helloworld;

public class TTH extends ClassFormat{
	private final int start = 1;
	private final int numOfDays = 2;
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
