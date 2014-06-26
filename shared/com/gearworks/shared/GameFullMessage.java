package com.gearworks.shared;

public class GameFullMessage extends Message {
	
	private String msg;
	
	public GameFullMessage(){
		msg("Im sorry, the game is full! You will remain connected but you will not be playing");
		//^ That message will be sent to any Clients who attempt to connect after both players have been assigned.
	}

	public String msg() {
		return msg;
	}

	public void msg(String msg) {
		this.msg = msg;
	}
	
	
}
