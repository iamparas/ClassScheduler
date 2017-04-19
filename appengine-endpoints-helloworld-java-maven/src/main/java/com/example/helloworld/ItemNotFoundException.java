package com.example.helloworld;

public class ItemNotFoundException extends RuntimeException{
	public ItemNotFoundException(String message){
		super(message);
	}
}
