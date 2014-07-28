package com.gearworks.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.gearworks.Game;
import com.gearworks.shared.Player;
import com.gearworks.shared.Player.Team;
import com.gearworks.state.InstanceInitState;
import com.gearworks.state.PlayerTurn;
import com.gearworks.state.ValidateTurn;
import com.gearworks.state.StateManager;
import com.gearworks.shared.Character;

/** An instance is the representation of a game in which two players have been matched */
public class Instance extends Listener{
	
	public static final int NUM_TURNS		=	10;	//Total number of turns in the game
	public static final int NUM_ROUNDS		=	3;	//Total number of rounds. A round = each player has a chance to be seeker & sneaker
	public static final int SEEKER_MOVES	=	4; //Number of moves each seeker character gets
	public static final int SNEAKER_MOVES	= 	5; //Number of moves the sneaker gets
	public static final Team STARTING_TEAM  =   Team.Sneaker;	//Team which goes first
	
	public Game game;
	
	private ServerPlayer[] 		players;			//Array containing matched players.
	private ServerPlayer 		activePlayer;		//The player whose turn it is.
	private int	   			turncount;			//Number of turns that have passed since the current round began
	private int				round;				//The current round
	private int				id;					//Used to ID instance
	private ServerLevel		level;				//Instance of the level we are on, used to validate poisitions and calculate vision
	private int				movesLeft;			//The number of moves the active player has left
	private String			mapName;			//Current map;
	
	private StateManager sm;				//Manages instance states:
											//		InstanceInitState: 	sends out the ConnectMessage to inform players that the instance has been created serverside
											//		PlayerTurn:		waits for players to make their turn
											//		ProcessTurn:		handle endturn logic
	
	public Instance(int id, ServerPlayer p1, ServerPlayer p2, Game game){
		this.id = id;
		System.out.println("Instance id: " + id);
		players 	= new ServerPlayer[]{p1, p2};
		players[0].team(Team.Sneaker);
		players[1].team(Team.Seeker);
		
		sm = new StateManager(game);
		sm().setState(new InstanceInitState(this));
	}
	
	public void update(){		
		sm.update();
	}
	
	public void playerDisconnected(Player pl){
		sm.state().onExit(game);
		sm.setState(null);
		
		//Send out game termination message
		
		dispose();
	}
	
	public void dispose(){
		level.dispose();
		sm = null;
	}
	
	//Returns true after both clients have completed the conection handshake
	public boolean clientsConnected() {
		return (players[0].instanceId() == id && players[1].instanceId() == id);
	}
	
	
	//Returns true after both clients have completed the round init handshake
	public boolean clientsReady(){
		return players[0].ready() && players[1].ready();
	}
	
	public void spawnPlayerCharacters(){
		players()[0].spawnCharacters(this);
		players()[1].spawnCharacters(this);
	}
	
	public boolean hasPlayer(Player pl){
		return (players[0].equals(pl) || players[1].equals(pl));
	}
	
	public int id(){ return id; }
	public ServerLevel level(){ return level; }
	public ServerPlayer[] players(){ return players; } 
	public ServerPlayer activePlayer(){ return activePlayer; }
	public void activePlayer(ServerPlayer pl){ activePlayer = pl; }

	public ServerPlayer getPlayerByConnection(Connection connection) {
		if(players[0].connection().equals(connection))
			return players[0];
		
		if(players[1].connection().equals(connection))
			return players[1];
		
		return null;
	}

	public StateManager sm() {
		return sm;
	}
	
	public void endTurnReceived(Vector2[] moves){
		//Update positions for active player
		ServerPlayer pl = activePlayer();
		
		sm.setState(new ValidateTurn(this, moves));
	}

	public int turncount() {
		return turncount;
	}

	public void turncount(int turncount) {
		this.turncount = turncount;
	}

	public int round() {
		return round;
	}

	public void round(int round) {
		this.round = round;
	}
	
	public int movesLeft(){
		return movesLeft;
	}
	
	public void movesLeft(int ml){
		movesLeft = ml;
	}

	public ServerPlayer getPlayerByTeam(Team startingTeam) {
		if(players()[0].team() == startingTeam){
			return players()[0];
		}else{
			return players()[1];
		}
	}
	
	public void mapName(String mapName){
		this.mapName = mapName;
	}
	
	public String mapName(){ return mapName; }
	
	public void level(ServerLevel level){ this.level = level; }
}
