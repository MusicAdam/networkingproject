package com.gearworks.state;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.gearworks.ClientListener;
import com.gearworks.Game;
import com.gearworks.game.ClientLevel;
import com.gearworks.game.Level;

//This state is responsible for ensuring that the player successfully connects to the server, and is matched in a game
public class ConnectState implements State {
	private static final int ID = 1;
	
	public boolean connectionEstablished = false; //True when a TCP connection has been made with the server

	private float timeout 	=  	30;	//Number of seconds we will try to establish connection to server
	private float timeSpent =	0;	//Amount of time spent trying to connect, or waiting to find a match (this is reset after connection has been made)
	private IOException exception;	//The exception thrown if the connection fails.
	
	@Override
	public void render(Game game) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Game game) {
		timeSpent += 1;
		
		if(!connectionEstablished){
			if(timeSpent * Game.STEP < timeout){
				if(timeSpent % 10 == 0){ //Attempt to connect every 10 frames
					try {
						game.client().addListener(new ClientListener(game, game.client()));
						game.client().connect(5000, "localhost", 60420, 60421);
						System.out.println("Connected in " + timeSpent * Game.STEP + " seconds");
						timeSpent = 0;
						connectionEstablished = true;
					} catch (IOException e) {
						exception = e;
					}
				}
			}else{
				System.out.println("Timeout: " + exception.getMessage());
			}
		}else{
			
			//Attempt to switch states to the init round state (wont happen until we are matched: see canExitState)
			game.setState(new InitRoundState());
		}
	}

	@Override
	public void onEnter(Game game) {
	}

	@Override
	public void onExit(Game game) {

	}

	@Override
	public boolean canEnterState(Game game) {
		return game.player() == null; //Don't enter unless no player object is created.
	}

	@Override
	public boolean canExitState(Game game) {
		return (game.player() != null && game.player().instanceId() != -1);
	}

	@Override
	public int getId() {
		return ID;
	}
}
