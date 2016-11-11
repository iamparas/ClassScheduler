package classscheduler;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.MaximumWeightBipartiteMatching;
import org.jgrapht.graph.SimpleWeightedGraph;

public class Handler {
	public static class RelationshipEdge<V> extends DefaultEdge{
		
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		LinkedList<Course> CourseQueue = new LinkedList<Course>();
		CourseQueue.add(new Course("Physics", "Phy101", 4, 10));
		CourseQueue.add(new Course("Chemistry", "Chem101", 4, 20));
		CourseQueue.add(new Course("Computer Science", "Phy101", 4, 40));
		CourseQueue.add(new Course("English", "Eng101", 2, 50));
		CourseQueue.add(new Course("Humanities", "HS101", 4, 20));
		CourseQueue.add(new Course("Natural Science", "NS101", 4, 30));
		CourseQueue.add(new Course("Political Science", "Phy101", 2, 15));

		Collections.sort(CourseQueue, new Comparator<Course>(){

			@Override
			public int compare(Course o1, Course o2) {
				if(o1.getCreditHour() < o2.getCreditHour()) return 1;
				else if(o1.getCreditHour() > o2.getCreditHour())return -1;
				else return 0;
			}
			
		});
		
	
		
//		Collections.sort(CourseQueue, new Comparator<Course>(){
//
//			@Override
//			public int compare(Course o1, Course o2) {
//				if(o1.getMaxAllowedEnrollment() < o2.getMaxAllowedEnrollment()) return 1;
//				else if(o1.getMaxAllowedEnrollment() > o2.getMaxAllowedEnrollment()) return -1;
//				else return 0;
//			}
//			
//		});
		
		System.out.println(CourseQueue);
		
		Set<CourseBlock> courseBlocks = new HashSet<CourseBlock>();

		boolean[] checked = new boolean[CourseQueue.size()];
		for(int i = 0; i < CourseQueue.size(); i++){
			if(checked[i]) continue;
			checked[i] = true;
			
			CourseBlock block = new CourseBlock();
			Course course = CourseQueue.get(i);
			block.addCourse(course, new MWF());
			for(int j = i + 1; j < CourseQueue.size(); j++){
				if(block.isMergeCompatible(CourseQueue.get(j), new TTH()) && !checked[j]){
					checked[j] = true;
					block.addCourse(CourseQueue.get(j), new TTH());
				}
			}
			
			courseBlocks.add(block);
		}
		
		
		WeightedGraph<Block, DefaultEdge> courseGraph = new SimpleWeightedGraph<Block, DefaultEdge>(DefaultEdge.class);
		for(Block block : courseBlocks){
			courseGraph.addVertex(block);
		}
		
		
		
		
		Set<RoomBlock> roomBlocks = new HashSet<RoomBlock>();
		String[] rooms = {"PJ121", "PJ122", "PJ123", "PJ124", "PJ221", "PJ222", "PJ223", "PJ322", "PJ323"};
		int[] roomsSize= {20, 30, 50, 30, 20, 40, 20, 30, 35};
		int[] times = {8, 10, 12, 1, 3, 5};
		
		for(int i = 0; i < rooms.length; i++){
			for(int j = 0; j < times.length - 1; j++){
				String startTime = times[j] + "";
				String endTime = times[j + 1] + "";
				roomBlocks.add(new RoomBlock(rooms[i], startTime, endTime, roomsSize[i]));
			}
		}
		
		
		for(RoomBlock block : roomBlocks){
			courseGraph.addVertex(block);
		}
		
		
		
		for(CourseBlock courseBlock : courseBlocks){
			for(RoomBlock roomBlock : roomBlocks){
				
				if(courseBlock.getMaxEnrollment() <= roomBlock.getMaxCapacity()){
					courseGraph.addEdge(courseBlock, roomBlock);
				}
			}
		}
		System.out.println(courseGraph.edgeSet());
		MaximumWeightBipartiteMatching maxWeightMatch = new MaximumWeightBipartiteMatching(courseGraph, courseBlocks, roomBlocks);
		
		System.out.println(maxWeightMatch.getMatching());
		//Block of classes
		// Block of classes 8:00 - 10:00 / 10:00 - 12:00 / 1:00 - 3:00 / 3:00 - 5:00 
		// Block List<Room[(start-time - end-time), room number, roomsize]>
		//
		// [8, 10, 12]
		// [1, 3, 5]
		// [PJ121, PJ122, PJ123, PJ124, PJ221, PJ222, PJ223, PJ322, PJ323]
		//Create each Block with 
		// 
		//
		//
		// abstract class block Block
		
		/* 
		 * Block
				abstract String toString();
		 * 
		 * RoomBlock extends Block
		 * 		String roomName;
		 * 		String startTime;
		 * 		String endTime;
		 * 		int roomSize;
		 * CourseBlock extends Block
		 * 		Course[][] blocks;
		 * 		int BLOCKSIZE;
		 * 		int WEEKDAYS;
		*/		
		
//		System.out.println(courseGraph);
		
	}

	
}
