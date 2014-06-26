package com.gearworks.shared;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gearworks.Game;
import com.gearworks.shared.Character;

public class Player {
	
	public enum Team{
		Seeker,
		Sneeker
	};
	
	private Team	team;		//Either seeker or sneeker	
							  	//or sneaking = false and you are the controlling the seekers
	private int 	score;			//fairly obvious, keeps track of score
	private boolean active;		//This keeps track of what player is active aka who's turn it is
	private int 	id;			//Unique player id assigned by the server
	private Array<Character>	characters;	//The player's characters.
	
	
	public Player(int id, Team team){
		this.team = team;
		this.id = id;
		characters = new Array<Character>();
	}
	
	public void spawnCharacters(Game game){
		if(team == Team.Sneeker){
			Character sneeker = (Character)game.spawn(new Character(this, game));
			sneeker.tile((int)game.level().getSneakerSpawn().x, (int)game.level().getSneakerSpawn().y);
			characters.add(sneeker);
		}else{
			for(Vector2 index : game.level().getSeekerSpawns()){
				Character seeker = (Character) game.spawn(new Character(this, game));
				seeker.tile((int)index.x, (int)index.y);
				characters.add(seeker);
			}
		}
	}
	
	public int id(){ return id; }
	public Team team(){ return team; }
	public Array<Character> characters(){ return characters; }
}
