package com.gearworks.game;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.gearworks.Game;
import com.gearworks.shared.Player;
import com.gearworks.state.InstanceInitState;
import com.gearworks.state.PlayerTurn;
import com.gearworks.state.StateManager;

/** An instance is the representation of a game in which two players have been matched */
public class Instance extends Listener{
	
	public static final int NUM_TURNS	=	10;	//Total number of turns in the game
	public static final int NUM_ROUNDS	=	3;	//Total number of rounds. A round = each player has a chance to be seeker & sneaker
	
	public Game game;
	
	private ServerPlayer[] 		players;			//Array containing matched players.
	private ServerPlayer 		activePlayer;		//The player whose turn it is.
	private int	   		turnsLeft;			//Number of turns left
	private int			round;				//The current round
	private int			id;					//Used to ID instance
	private ServerLevel		level;				//Instance of the level we are on, used to validate poisitions and calculate vision
	
	private StateManager sm;				//Manages instance states:
											//		InstanceInitState: 	sends out the ConnectMessage to inform players that the instance has been created serverside
											//		PlayerTurn:		waits for players to make their turn
											//		ProcessTurn:		handle endturn logic
	
	public Instance(int id, ServerPlayer p1, ServerPlayer p2, Game game){
		this.id = id;
		System.out.println("Instance id: " + id);
		players 	= new ServerPlayer[]{p1, p2};
		turnsLeft 	= NUM_TURNS;
		round		= 1;
		
		level = new ServerLevel(game);
		level.load("assets/map1.tmx");
		p1.spawnCharacters(this);
		p2.spawnCharacters(this);
		
		sm = new StateManager(game);
		sm.setState(new InstanceInitState(this));
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
	public boolean clientsReady() {
		System.out.println("CLient is ready: " + (players[0].instanceId() == id && players[1].instanceId() == id));
		return (players[0].instanceId() == id && players[1].instanceId() == id);
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
}
