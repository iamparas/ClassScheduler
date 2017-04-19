/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.example.helloworld;
import static com.example.helloworld.service.OfyService.ofy;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.inject.Named;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.googlecode.objectify.Key;

class Output{
	public String schedule;
	
	public Output(String schedule){
		this.schedule = schedule;
	}
	
	public String getSchedule(){
		return schedule;
	}
}
// [START header]
/** An endpoint class we are exposing */
@Api(name = "myApi", version = "v1", namespace = @ApiNamespace(ownerDomain = "helloworld.example.com", ownerName = "helloworld.example.com", packagePath = ""), clientIds = {
		Constants.WEB_CLIENT_ID, Constants.ANDROID_CLIENT_ID,
		Constants.IOS_CLIENT_ID }, audiences = { Constants.ANDROID_AUDIENCE })
// [END header]

public class YourFirstAPI {
    private static final Logger LOG = Logger.getLogger(YourFirstAPI.class.getName());

	@ApiMethod(name = "addCourse", path = "addCourse", httpMethod = ApiMethod.HttpMethod.POST)

	public Course addCourse(Course course) throws Exception {
		if(course == null) throw new IllegalArgumentException("Course is null");
		ofy().save().entity(course).now();
		return course;
	}
	
	@ApiMethod(name = "getCourseByCourseNumber", path = "course/{courseNumber}", httpMethod = ApiMethod.HttpMethod.GET)

	public Course getCourseByCourseNumber(@Named("courseNumber") final String courseNumber) throws Exception{
		if(courseNumber.isEmpty() || courseNumber == null) throw new IllegalArgumentException("Invalid courseNumber");
		Key key = Key.create(Course.class, courseNumber);
		Course course =  (Course) ofy().load().key(key).now();
		if(course == null){
			String message = String.format("Course with coursenumber %s not found", courseNumber);
			LOG.warning(message);
			throw new ItemNotFoundException(message);		
		}
		return course; 
	}
	
	@ApiMethod(name = "getAllCourses", path = "course/all", httpMethod = ApiMethod.HttpMethod.GET)
	
	public List<Course> getAllCourses() throws Exception{
		List<Course> courses = ofy().load().type(Course.class).list();
		if(courses.isEmpty()){
			throw new ItemNotFoundException("No courses Found");
		}
		return courses;
	}
	@ApiMethod(name = "addRoom", path = "room", httpMethod = ApiMethod.HttpMethod.POST)
	
	public Room addRoom(Room room) throws Exception{
		if(room == null) throw new Exception("Room Shouldn't be null");
		ofy().save().entity(room).now();
		return room;
	}
	
	@ApiMethod(name = "getAllRooms", path = "room/all", httpMethod = ApiMethod.HttpMethod.GET)
	public List<Room> getAllRooms() throws Exception{
		List<Room> rooms = ofy().load().type(Room.class).list();
		if(rooms.isEmpty()){
			throw new ItemNotFoundException("No courses Found");
		}
		return rooms;
	}

	@ApiMethod(name = "getSchedule", path="schedule", httpMethod = ApiMethod.HttpMethod.GET)
	
	public Output getSchedule() throws Exception{
		List<Course> courseQueue = getAllCourses();
 		Set<CourseBlock> courseBlocks = Handler.generateCourseBlocks(courseQueue);
		//List<Room> roomQueue = getAllRooms();
		//Set<RoomBlock> roomBlocks = Handler.generateRoomBlocks(roomQueue);
 		Set<RoomBlock> roomBlocks = Handler.generateTestRoomBlocksForAPI();
 		String out = Handler.generateSchedule(courseBlocks, roomBlocks);
 		LOG.warning(out);
 		return new Output(out);
 		
		
	}
	

}
