package com.elliot.reddit.exception;

public class SubredditNotFoundException extends RuntimeException {
	public SubredditNotFoundException(String message) {
		super(message);
	}
}