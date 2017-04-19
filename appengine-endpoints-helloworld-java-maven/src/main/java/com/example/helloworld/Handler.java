package com.example.helloworld;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.jgraph.graph.Edge;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.interfaces.MatchingAlgorithm.Matching;
import org.jgrapht.alg.matching.MaximumWeightBipartiteMatching;
import org.jgrapht.graph.ClassBasedEdgeFactory;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.google.appengine.repackaged.org.joda.time.LocalTime;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

class ClassEnrollmentComparator implements Comparator<CourseBlock> {
	public int compare(CourseBlock c1, CourseBlock c2) {
		return Integer.compare(c1.getMaxEnrollment(), c2.getMaxEnrollment());
	}
}

class Event {
	@Expose
	String startTime;
	@Expose
	String endTime;
	@Expose
	String courseName;
	@Expose
	String courseID;

	public Event(TimeSlot time, Course course) {
		this.startTime = time.startTime.toString("HH:mm");
		this.endTime = time.endTime.toString("HH:mm");
		this.courseName = course.getCourseName();
		this.courseID = course.getCourseNumber();

	}
}

public class Handler {
	public static HashMap<String, List<Event>> getEventFromCourseBlock(CourseBlock cb, TimeSlot slot) {
		HashMap<String, List<Event>> DayToEvent = new HashMap<String, List<Event>>();
		String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
		for (String day : daysOfWeek) {
			DayToEvent.put(day, new ArrayList<Event>());
		}
		for (int weekday = 0; weekday < cb.WEEKDAYS; weekday++) {
			String day = daysOfWeek[weekday];
			for (int i = 0; i < cb.BLOCKSIZE; i++) {
				Course course = cb.getCourse(i, weekday);
				if (course != null) {
					List<Event> events = DayToEvent.get(day);
					events.add(new Event(slot, course));
					DayToEvent.put(day, events);
				}
			}
		}
		// for(String key : DayToEvent.keySet()){
		// System.out.printf("Key %s : %s ", key, DayToEvent.get(key));
		// }
		return DayToEvent;
	}

	public static double getWeight(List<Feature> features) {
		int weight = 0;
		for (Feature f : features) {
			// System.out.println(f.getClass() + " " + f.getValue() + " "+
			// f.getWeight());
			weight += f.getValue() * f.getWeight();
		}
		return weight;
	}

	public static Set<CourseBlock> generateCourseBlocks(List<Course> CourseQueue) {
		Collections.sort(CourseQueue, new Comparator<Course>() {
			public int compare(Course o1, Course o2) {
				if ((o1.getCreditHour() < o2.getCreditHour()))
					return 1;
				else if (o1.getCreditHour() > o2.getCreditHour())
					return -1;
				else
					return 0;
			}

		});
		Set<CourseBlock> courseBlocks = new HashSet<CourseBlock>();
		boolean[] checked = new boolean[CourseQueue.size()];
		for (int i = 0; i < CourseQueue.size(); i++) {
			if (checked[i])
				continue;
			checked[i] = true;

			CourseBlock block = new CourseBlock();
			Course course = CourseQueue.get(i);
			block.addCourse(course, new MWF());
			for (int j = i + 1; j < CourseQueue.size(); j++) {
				if (block.isMergeCompatible(CourseQueue.get(j), new TTH()) && !checked[j]) {
					checked[j] = true;
					block.addCourse(CourseQueue.get(j), new TTH());
				}
			}
			courseBlocks.add(block);
		}
		return courseBlocks;
	}
	public static Set<RoomBlock> generateTestRoomBlocksForAPI(){
		//String roomNumber, String buildingName, int roomSize){
		Room pj102 = new Room("102", "Park Johnson", 25);
		Room pj103 = new Room("103", "Park Johnson", 40);
		Room pj104 = new Room("104", "Park Johnson", 20);
		Room pj201 = new Room("201", "Park Johnson", 50);
		Room pj301 = new Room("301", "Park Johnson", 25);
		Room pj302 = new Room("302", "Park Johnson", 25);
		return generateRoomBlocks(new ArrayList<Room>(Arrays.asList(pj102, pj103, pj104, pj201, pj301, pj302)));
	}
	public static Set<RoomBlock> generateRoomBlocks(List<Room> rooms) {
		Set<RoomBlock> roomBlocks = new LinkedHashSet<RoomBlock>();
		int[] times = { 8, 10, 12, 1, 3, 5 };
		for (int i = 0; i < rooms.size(); i++) {
			for (int j = 0; j < times.length - 1; j++) {
				int startTime = times[j];
				int endTime = times[j + 1];
				if (startTime == 12)
					continue; // No Rooms Available for class during lunch
								// break
				roomBlocks.add(new RoomBlock(rooms.get(i), new TimeSlot(startTime, 0, endTime, 0)));
			}
		}
		return roomBlocks;
	}

	public static String generateSchedule(Set<CourseBlock> courseBlocks, Set<RoomBlock> roomBlocks) {
		WeightedGraph<Block, RelationshipEdge> courseGraph = new SimpleWeightedGraph<Block, RelationshipEdge>(
				new ClassBasedEdgeFactory<Block, RelationshipEdge>(RelationshipEdge.class));

		for (Block block : courseBlocks) {
			courseGraph.addVertex(block);
		}

		for (RoomBlock block : roomBlocks) {
			courseGraph.addVertex(block);
		}

		// Data points necessary to calculate the soft constraints
		int MAX_ENROLLMENT = Collections.max(courseBlocks, new ClassEnrollmentComparator()).getMaxEnrollment();
		int MIN_ENROLLMENT = Collections.min(courseBlocks, new ClassEnrollmentComparator()).getMaxEnrollment();

		/* Weight Distribution among features */
		StudentBodySizeFeature.WEIGHT = 0.5;
		PreferredTimeFeature.WEIGHT = 0.5;
		for (CourseBlock courseBlock : courseBlocks) {

			for (RoomBlock roomBlock : roomBlocks) {
				// boolean isPreferredRoomBlock =
				// courseBlock.isPreferredTimeBlock;
				RelationshipEdge<Block> edge = new RelationshipEdge<Block>(courseBlock, roomBlock);

				if (edge.isCompatible()) {
					courseGraph.addEdge(courseBlock, roomBlock, edge);
					List<Course> courses = courseBlock.getCourses();
					int c = courses.get(0).getMaxAllowedEnrollment();
					StudentBodySizeFeature sbf = new StudentBodySizeFeature(c, MAX_ENROLLMENT, MIN_ENROLLMENT);
					PreferredTimeFeature ptf = new PreferredTimeFeature(courses.get(0).getPreferences(),
							roomBlock.getTimeSlot());
					List<Feature> features = new ArrayList<Feature>(Arrays.asList(sbf, ptf));
					courseGraph.setEdgeWeight(edge, getWeight(features));
				}
			}
		}
		MaximumWeightBipartiteMatching bipartiteAlgorithm = new MaximumWeightBipartiteMatching(courseGraph,
				courseBlocks, roomBlocks);
		Matching<RelationshipEdge> matches = bipartiteAlgorithm.computeMatching();
Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

		
		HashMap<String, HashMap<String, List<Event>>> RoomToSchedule = new HashMap<String, HashMap<String, List<Event>>>();
		
		for (RelationshipEdge e : matches.getEdges()) {
			RoomBlock r = e.getRoomBlock();
			CourseBlock c = e.getCourseBlock();
			String RoomToScheduleKey = r.getRoomID();
			HashMap<String, List<Event>> DayToEventsFromCurrent = getEventFromCourseBlock(c, r.getTimeSlot());
			if (RoomToSchedule.containsKey(RoomToScheduleKey)) {

				
				HashMap<String, List<Event>> DayToEventsFromSchedule = RoomToSchedule.get(RoomToScheduleKey);
				for(String dayOfWeek : DayToEventsFromSchedule.keySet()){
					List<Event> currList = DayToEventsFromSchedule.get(dayOfWeek);
					Collections.copy(currList, DayToEventsFromCurrent.get(dayOfWeek));
					DayToEventsFromSchedule.put(dayOfWeek, currList);
				}
				RoomToSchedule.put(RoomToScheduleKey, DayToEventsFromSchedule);

			}else{
				RoomToSchedule.put(RoomToScheduleKey, DayToEventsFromCurrent);
			}
		}
		return gson.toJson(RoomToSchedule);
		
	}

	public static boolean isCloser(int a, int b, int difference) {
		return Math.abs(a - b) < 10;
	}

	public static Set<CourseBlock> getTestCourseBlocks() {
		Course physics = new Course("Physics", "Phy101", 4, 10);
		physics.setPreference(8, 00, 10, 00);
		physics.setPreference(10, 00, 12, 00);
		Course cs = new Course("Computer Science", "CS101", 4, 40);
		cs.setPreference(10, 00, 12, 00);
		Course chem = new Course("Chemistry", "Chem101", 4, 20);
		chem.setPreference(10, 00, 12, 00);
		Course english = new Course("English", "Eng101", 2, 50);
		english.setPreference(13, 00, 15, 00);
		Course humanities = new Course("Humanities", "HS101", 4, 20);
		humanities.setPreference(13, 00, 15, 00);
		humanities.setPreference(10, 00, 12, 00);
		Course natsci = new Course("Natural Science", "NS101", 4, 30);
		Course polysci = new Course("Political Science", "Poly201", 2, 15);

		LinkedList<Course> CourseQueue = new LinkedList<Course>();
		CourseQueue.add(physics);
		CourseQueue.add(cs);
		CourseQueue.add(chem);
		CourseQueue.add(english);
		CourseQueue.add(humanities);
		CourseQueue.add(natsci);
		CourseQueue.add(polysci);

		// System.out.println(getHotSlots(CourseQueue));
		/*
		 * Grouping courses based on credit hour, so they can be merged later
		 */

		Collections.sort(CourseQueue, new Comparator<Course>() {
			public int compare(Course o1, Course o2) {
				if ((o1.getCreditHour() < o2.getCreditHour()))
					return 1;
				else if (o1.getCreditHour() > o2.getCreditHour())
					return -1;
				else
					return 0;
			}

		});

		System.out.println(CourseQueue);

		Set<CourseBlock> courseBlocks = new HashSet<CourseBlock>();

		boolean[] checked = new boolean[CourseQueue.size()];
		for (int i = 0; i < CourseQueue.size(); i++) {
			if (checked[i])
				continue;
			checked[i] = true;

			CourseBlock block = new CourseBlock();
			Course course = CourseQueue.get(i);
			block.addCourse(course, new MWF());
			for (int j = i + 1; j < CourseQueue.size(); j++) {
				if (block.isMergeCompatible(CourseQueue.get(j), new TTH()) && !checked[j]) {
					checked[j] = true;
					block.addCourse(CourseQueue.get(j), new TTH());
				}
			}
			courseBlocks.add(block);
		}
		return courseBlocks;
	}

	public static List<TimeSlot> getHotSlots(List<Course> courses) {
		HashMap<TimeSlot, Integer> TimeSlotToPreferenceCount = new HashMap<TimeSlot, Integer>();
		for (Course course : courses) {
			for (TimeSlot timeslot : course.getPreferences()) {
				if (TimeSlotToPreferenceCount.containsKey(timeslot)) {
					int currentCount = TimeSlotToPreferenceCount.get(timeslot);
					TimeSlotToPreferenceCount.put(timeslot, ++currentCount);
				} else {
					TimeSlotToPreferenceCount.put(timeslot, 0);
				}
			}
		}

		// TODO Sort the list with the highest count, maybe instead use a
		// TreeMap
		return new ArrayList<TimeSlot>(TimeSlotToPreferenceCount.keySet());
	}

	@SuppressWarnings("deprecation")
	// public static int createEdgeWeight(int classSize, int courseNumber,
	// boolean isInPreferredSlot){
	// int normalizedClassSize = classSize - minClassSize / maxClassSize -
	// minClassSize;
	// }

	public static Set<RoomBlock> getTestRoomBlocks() {
		List<Room> rooms = new ArrayList<Room>();
		rooms.add(new Room("101", "Park Johnson", 30));
		rooms.add(new Room("122", "Park Johnson", 60));
		rooms.add(new Room("201", "Park Johnson", 20));
		rooms.add(new Room("301", "Park Johnson", 10));
		rooms.add(new Room("303", "Park Johnson", 10));
		rooms.add(new Room("311", "Park Johnson", 20));
		rooms.add(new Room("322", "Park Johnson", 30));
		rooms.add(new Room("102", "Park Johnson", 20));
		rooms.add(new Room("103", "Park Johnson", 30));

		Set<RoomBlock> roomBlocks = new LinkedHashSet<RoomBlock>();
		int[] times = { 8, 10, 12, 1, 3, 5 };

		for (int i = 0; i < rooms.size(); i++) {
			for (int j = 0; j < times.length - 1; j++) {
				int startTime = times[j];
				int endTime = times[j + 1];
				if (startTime == 12)
					continue; // No Rooms Available for class during lunch
								// break
				roomBlocks.add(new RoomBlock(rooms.get(i), new TimeSlot(startTime, 0, endTime, 0)));
			}
		}
		return roomBlocks;
	}

	public static int normalize(int val, int max, int min) {
		return (val - min) / (max - min);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		WeightedGraph<Block, RelationshipEdge> courseGraph = new SimpleWeightedGraph<Block, RelationshipEdge>(
				new ClassBasedEdgeFactory<Block, RelationshipEdge>(RelationshipEdge.class));

		Set<CourseBlock> courseBlocks = getTestCourseBlocks();

		for (Block block : courseBlocks) {
			courseGraph.addVertex(block);
		}

		Set<RoomBlock> roomBlocks = getTestRoomBlocks();

		for (RoomBlock block : roomBlocks) {
			courseGraph.addVertex(block);
		}

		int MAX_ENROLLMENT = Collections.max(courseBlocks, new ClassEnrollmentComparator()).getMaxEnrollment();
		int MIN_ENROLLMENT = Collections.min(courseBlocks, new ClassEnrollmentComparator()).getMaxEnrollment();

		/* Weight Distribution among features */
		StudentBodySizeFeature.WEIGHT = 0.5;
		PreferredTimeFeature.WEIGHT = 0.5;
		for (CourseBlock courseBlock : courseBlocks) {

			for (RoomBlock roomBlock : roomBlocks) {
				// boolean isPreferredRoomBlock =
				// courseBlock.isPreferredTimeBlock;
				RelationshipEdge<Block> edge = new RelationshipEdge<Block>(courseBlock, roomBlock);

				if (edge.isCompatible()) {
					courseGraph.addEdge(courseBlock, roomBlock, edge);
					List<Course> courses = courseBlock.getCourses();
					int c = courses.get(0).getMaxAllowedEnrollment();
					StudentBodySizeFeature sbf = new StudentBodySizeFeature(c, MAX_ENROLLMENT, MIN_ENROLLMENT);
					PreferredTimeFeature ptf = new PreferredTimeFeature(courses.get(0).getPreferences(),
							roomBlock.getTimeSlot());

					List<Feature> features = new ArrayList<Feature>(Arrays.asList(sbf, ptf));
					courseGraph.setEdgeWeight(edge, getWeight(features));
				}
			}
		}
		MaximumWeightBipartiteMatching bipartiteAlgorithm = new MaximumWeightBipartiteMatching(courseGraph,
				courseBlocks, roomBlocks);
		Matching<RelationshipEdge> matches = bipartiteAlgorithm.computeMatching();
		Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

		
		HashMap<String, HashMap<String, List<Event>>> RoomToSchedule = new HashMap<String, HashMap<String, List<Event>>>();
		
		for (RelationshipEdge e : matches.getEdges()) {
			RoomBlock r = e.getRoomBlock();
			CourseBlock c = e.getCourseBlock();
			String RoomToScheduleKey = r.getRoomID();
			HashMap<String, List<Event>> DayToEventsFromCurrent = getEventFromCourseBlock(c, r.getTimeSlot());
			if (RoomToSchedule.containsKey(RoomToScheduleKey)) {

				
				HashMap<String, List<Event>> DayToEventsFromSchedule = RoomToSchedule.get(RoomToScheduleKey);
				for(String dayOfWeek : DayToEventsFromSchedule.keySet()){
					List<Event> currList = DayToEventsFromSchedule.get(dayOfWeek);
					Collections.copy(currList, DayToEventsFromCurrent.get(dayOfWeek));
					DayToEventsFromSchedule.put(dayOfWeek, currList);
				}
				RoomToSchedule.put(RoomToScheduleKey, DayToEventsFromSchedule);

			}else{
				RoomToSchedule.put(RoomToScheduleKey, DayToEventsFromCurrent);
			}
		}
		System.out.println(gson.toJson(RoomToSchedule));

	}

}
