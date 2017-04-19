package com.example.helloworld;

public class StudentBodySizeFeature implements Feature{
	private int classSize;
	private int maxClassSize;
	private int minClassSize;
	public static double WEIGHT;
	private static final int multiplier = 100;

	public StudentBodySizeFeature(int classSize, int maxClassSize, int minClassSize){
		this.classSize = classSize;
		this.maxClassSize = maxClassSize;
		this.minClassSize = minClassSize;
	}
	
	public double getWeight() {
		if(WEIGHT == 0) throw new ItemNotFoundException("Weight value is not set");
		return WEIGHT;
	}
	
	public static void setWeight(double weight){
		StudentBodySizeFeature.WEIGHT = weight;
	}
	
	public double getValue() {
		return (double)(maxClassSize - classSize)/( maxClassSize - minClassSize) * multiplier;
	}

}
