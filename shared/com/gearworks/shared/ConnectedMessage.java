package com.gearworks.shared;

/*
 * Goblins would use this class to send a message from the server to the client saying that they connected successfully
 * 
 * Goblins would also then get their pid from this message, allowing the players to be distinguished
 */

public class ConnectedMessage extends Message {

	private int pid;
	
	public ConnectedMessage(int id) {
		pid = id;
	}

	//TODO anything
	
}
