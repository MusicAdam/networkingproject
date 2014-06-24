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
	
	ArrayList<Vector2> positions;
	
	

	public EndTurn(ArrayList<Vector2> pos){
		//I guess all this needs is the positions of the units
		for(Vector2 v : pos){
			positions.add(v);
		}
		
	}
	public ArrayList<Vector2> positions() {
		return positions;
	}

	public void positions(ArrayList<Vector2> positions) {
		this.positions = positions;
	}
		
}
