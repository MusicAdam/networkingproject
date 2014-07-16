package com.gearworks.shared;

import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;

/*
 * I am a comment plozox
 * This message is sent from the client to the server to indicate that the Client has finished its turn
 * 
 * 
 */


public class EndTurn extends Message {
	
	private int index;
	
	
	public EndTurn(int i){
		index(i);
	}


	public int index() {
		return index;
	}


	public void index(int index) {
		this.index = index;
	}
		
}
