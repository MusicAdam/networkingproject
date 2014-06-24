package com.gearworks;

public class Player {
	
	private boolean sneaking; 	//either sneaking = true and you are the sneaker
							  	//or sneaking = false and you are the controlling the seekers
	private int score;			//fairly obvious, keeps track of score
	private boolean active;		//This keeps track of what player is active aka who's turn it is
	private String name;
	
	
	//TODO this will be for the server to know who's turn it is and determine turn order and all that Jazz
	
	public Player(String n){
		name = n;
		score = 0;
	}
	
	
}
