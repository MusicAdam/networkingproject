package com.gearworks.shared;

/*
 * Goblins would use this class to send a message from the server to the client saying that they connected successfully
 * 
 * Goblins would also then get their pid from this message, allowing the players to be distinguished
 */

public class ConnectedMessage extends Message {

	private Player player;
	
	public ConnectedMessage(Player p) {
		player(p);
	}

	public Player player() {
		return player;
	}

	public void player(Player player) {
		this.player = player;
	}

	//TODO anything (not really)
	
}
