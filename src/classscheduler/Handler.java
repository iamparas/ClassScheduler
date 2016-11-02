package classscheduler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class Handler {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		List<Course> CourseQueue = new ArrayList<Course>();
		Collections.sort(CourseQueue, new Comparator<Course>(){

			@Override
			public int compare(Course o1, Course o2) {
				if(o1.getCreditHour() > o2.getCreditHour()) return 1;
				else if(o1.getCreditHour() < o2.getCreditHour())return -1;
				else return 0;
			}
			
		});
		LinkedList<Block> blocks = new LinkedList<Block>();
		for(Course course : CourseQueue){
			Block b = new Block();
			b.addCourse(course, ClassFormat.MWF);
			blocks.add(b);
		}
		List<Block> processedBlocks = new ArrayList<Block>();
		while(!blocks.isEmpty()){
			Block curr = blocks.poll();
			
			for(Block b : blocks){
				if(curr.isMergeCompatible(b)){
					curr.merge(b);
					blocks.remove(b); 
				}
			}
			processedBlocks.add(curr);
		}
		

	}

}
