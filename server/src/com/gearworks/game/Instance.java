package com.gearworks.game;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Listener;
import com.gearworks.Game;
import com.gearworks.shared.Level;
import com.gearworks.shared.Player;
import com.gearworks.state.StateManager;

/** An instance is the representation of a game in which two players have been matched */
public class Instance extends Listener{
	
	public static final int NUM_TURNS	=	10;	//Total number of turns in the game
	public static final int NUM_ROUNDS	=	3;	//Total number of rounds. A round = each player has a chance to be seeker & sneaker
	
	
	private Player[] 	players;			//Array containing matched players.
	private Player 		activePlayer;		//The player whose turn it is.
	private int	   		turnsLeft;			//Number of turns left
	private int			round;				//The current round
	private int			id;					//Used to ID instance
	private Level		level;				//Instance of the level we are on, used to validate poisitions and calculate vision
	
	private StateManager sm;				//Manages instance states:
											//		InstanceInitState: 	sends out the ConnectMessage to inform players that the instance has been created serverside
											//		WaitForPlayer:		waits for players to make their turn
											//		ProcessTurn:		handle endturn logic
	
	public Instance(Player p1, Player p2, Game game){
		players 	= new Player[]{p1, p2};
		turnsLeft 	= NUM_TURNS;
		round		= 1;
		
		level = new Level(game);
		level.load(1);
		
		game.server().addListener(this);
		
		sm = new StateManager(game);
	}

	//Returns true after both clients have completed the conection handshake
	public boolean clientsReady() {
		return (players[0].instanceId() == id && players[1].instanceId() == id);
	}
	
	
	
	
}
