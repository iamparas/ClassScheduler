package classscheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class GreedyClassScheduling {
	
	class EndTimeComparator implements Comparator<Class>{

		public int compare(Class o1, Class o2) {
			if(o1.endTime == o2.endTime) return 0;
			else if(o1.endTime > o2.endTime) return 1;
			else return -1;
		}
	
	}
	
	public ArrayList<Class> optimal(ArrayList<Class> classes){
		Collections.sort(classes, new EndTimeComparator());
		Class prev = classes.get(0);
		ArrayList<Class> optimalClasses = new ArrayList<Class>();
		for(int i = 1; i < classes.size(); i++){
			Class next = classes.get(i);
			if(prev.endTime < next.endTime){
				optimalClasses.add(next);
				prev = next;
			}
		}
		return optimalClasses;
		
	}
	
	
	public static void main(String[] args){
		GreedyClassScheduling s = new GreedyClassScheduling();
		ArrayList<Class> classes = new ArrayList<Class>(Arrays.asList(new Class(1, 4), new Class(2, 3),
																	  new Class(6, 7), new Class(8, 9)));
		for(Class cls : s.optimal(classes)){
			System.out.printf("start-time :  %d end-time : %d\n", cls.startTime, cls.endTime);
		}
	}
}
