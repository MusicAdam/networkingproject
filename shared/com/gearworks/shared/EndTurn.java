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
	
	private Vector2[] indices;
	public int instanceId;
	
	
	public EndTurn(){
		indices = new Vector2[3];
	}


	public Vector2[] indices() {
		return indices;
	}


	public void index(int i, Vector2 index) {
		indices[i] = index;
	}
		
}
