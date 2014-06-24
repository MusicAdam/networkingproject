package com.gearworks.shared;

import com.badlogic.gdx.math.Vector2;

/*
 * StartTurn is sent by the Server to the Client to tell the Client it is there turn
 * 
 * I imagine this will flip the boolean 'active' allowing the player to make their move
 * 
 * 
 */


public class StartTurn extends Message {
	public Player active;
	public Vector2[] hiddenCells;    //Locations that the player cannot see
	public Vector2[] visibleEnemies; //Locations of enemies (seeker/seeker will be determined clientside)
}
