package com.gearworks.shared;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.esotericsoftware.kryonet.Connection;
import com.gearworks.Game;
import com.gearworks.shared.Character;

public class Player {
	
	public enum Team{
		Seeker,
		Sneaker
	};
	
	protected Team	team;		//Either seeker or sneeker	
							  	//or sneaking = false and you are the controlling the seekers
	private int 	score;		//fairly obvious, keeps track of score
	protected boolean active;		//This keeps track of what player is active aka who's turn it is
	protected boolean ready;			//True when InitRoundMessage is processed
	private int 	instanceId;			//Unique instace id assigned by the server once a connection has been made
	protected Array<Character>	characters;	//The player's characters.
	protected Connection connection;
	
	public Player(Connection c){
		this.connection = c;
		characters = new Array<Character>();
		instanceId = -1;
	}
	
	public void spawnCharacters(Game game){
		if(team == Team.Sneaker){
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
	
	public int instanceId(){ return instanceId; }
	public void instanceId(int id){ instanceId = id; }
	public Team team(){ return team; }
	public void team(Team t){ team = t; }
	public Array<Character> characters(){ return characters; }
	public void addCharacter(Character c){ characters.add(c); }
	public void clearCharacters(){ characters.clear(); }
	public void removeCharacter(Character c){ characters.removeValue(c, true); }
	public Connection connection(){return connection;}
	public void ready(boolean r){ ready = r; }
	public boolean ready(){ return ready; }

	public int score() {
		return score;
	}

	public void score(int score) {
		this.score = score;
	}
}
