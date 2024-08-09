package com.crow.ssecurity.exception;

import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;

public class UserNotFoundException extends NotFoundException{
	String message;
	
	public UserNotFoundException(String message) {
		this.message=message;
	}
}
