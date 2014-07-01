package com.gearworks;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.gearworks.game.Instance;
import com.gearworks.game.ServerPlayer;
import com.gearworks.shared.*;


public class ServerListener extends Listener{
	
	private Server server;
	private Game game;
	private int pidGen;
	
	public ServerListener(Server s, Game g){
		game = g;
		server = s;
		pidGen = -1;
	}
	
	@Override
	public void connected(Connection connection){
		ServerPlayer player = new ServerPlayer(connection);
		game.addPlayer(player);
	}
	
	
	@Override
	public void disconnected(Connection connection){
		ServerPlayer pl = game.findPlayerByConnection(connection);
		//pl.dispose(); 
		game.removePlayer(pl);
	}
	
	@Override
	 public void received (Connection connection, Object object) {
		
		if(object instanceof ConnectMessage){
			ConnectMessage msg = (ConnectMessage)object;
			
			Instance instance = game.getInstance(msg.instanceId);
			Player pl = instance.getPlayerByConnection(connection);
			pl.instanceId(msg.instanceId);
			System.out.println("Player inst in list: " + pl.instanceId());
		}
		
		/*
		 * This will be handled in instance
		 
		if(object instanceof ConnectMessage){
			pidGen++;
			if(pidGen <= 1){
				if(pidGen == 0){
					game.activePlayer(new Player(pidGen, connection));
					server.sendToTCP(pidGen, new ConnectedMessage(game.activePlayer()));
				}
				else if(pidGen == 1){
					game.inactivePlayer(new Player(pidGen, connection));
					server.sendToTCP(pidGen, new ConnectedMessage(game.inactivePlayer()));
				}
			}
			else{
				server.sendToTCP(pidGen, new GameFullMessage());
			}
				
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
			
		}*/
	}
}
