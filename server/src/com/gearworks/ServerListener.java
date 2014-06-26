package com.gearworks;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.gearworks.shared.*;


public class ServerListener extends Listener{
	
	private Server server;
	private Game game;
	private int pidGen;
	
	public ServerListener(Server s, Game g){
		game = g;
		server = s;
	}
	
	@Override
	 public void received (Connection connection, Object object) {
		
		if(object instanceof ConnectMessage){
			server.sendToTCP(pidGen, new ConnectedMessage(pidGen));
			pidGen++;
		}
		else if(object instanceof EndTurn){
			//Check to see if move is legal
			//if it is legal, update board and send next StartTurn message
			//the player that is active becomes inactive, and the other becomes active.
			//check victory conditions
			EndTurn endt = (EndTurn) object;
			for(Vector2 v : endt.positions()){
				if(game.level().isWall((int)v.x, (int)v.y)){
					InvalidMove im = new InvalidMove();
					server.sendToAllTCP(im);
				}
				else{ //otherwise send a StartTurn to the other player
					if(game.checkVictory()){
						//TODO the game ends
					}
					Player temp = game.inactivePlayer(); 		//assign active to temp
					game.inactivePlayer(game.activePlayer());	//assign inactive as last active
					game.activePlayer(temp);					//assign new active to last inactive
					//TODO Send StartTurn message to all
				}//end else
			}//end for
			
		}
	}
}
