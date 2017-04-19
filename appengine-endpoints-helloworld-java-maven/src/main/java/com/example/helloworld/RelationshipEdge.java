package com.example.helloworld;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;

public class RelationshipEdge<Block>  extends DefaultWeightedEdge{
	private CourseBlock v1;
	private RoomBlock v2;

	public RelationshipEdge(Block v1, Block v2) {
		this.v1 = (CourseBlock) v1;
		this.v2 = (RoomBlock) v2;
	}

	public String toString() {
		return String.format("%s -> %s", v1.toString(), v2.toString());
	}
	
	public CourseBlock getCourseBlock(){
		return v1;
	}
	public RoomBlock getRoomBlock(){
		return v2;
	}

	public boolean isCompatible() {
		return v1.getMaxEnrollment() <= v2.getMaxCapacity() && !v2.isReserved();
	}
}