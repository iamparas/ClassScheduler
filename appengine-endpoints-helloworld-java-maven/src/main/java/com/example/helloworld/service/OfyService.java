package com.example.helloworld.service;
import com.example.helloworld.Course;
import com.example.helloworld.Room;
import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

public class OfyService {
	static{
		factory().register(Course.class);
		factory().register(Room.class);
	}
	 /**
     * Use this static method for getting the Objectify service object in order to make sure the
     * above static block is executed before using Objectify.
     * @return Objectify service object.
     */
	public static Objectify ofy(){
		return ObjectifyService.ofy();
	}
	
	public static ObjectifyFactory factory(){
		return ObjectifyService.factory();
	}
}
