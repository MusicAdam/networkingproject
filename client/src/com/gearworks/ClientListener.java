package com.gearworks;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.gearworks.game.ClientLevel;
import com.gearworks.shared.*;
import com.gearworks.state.ConnectState;

public class ClientListener extends Listener{
	
	private Game game;
	private Client client;
	
	public ClientListener(Game g, Client c){
		
		game = g;
		client = c;
		
	}
	
	@Override
	public void disconnected(Connection connection){
		System.out.println("Disconnected");
	}
	
	@Override
	public void connected(Connection connection){
		System.out.println("Connected");
	}
	
	public void received (Connection connection, Object object){
		if(object instanceof GameFullMessage){
			//SOMEHOW DISPLAY IT TO THE PLAYERS
			GameFullMessage gfm = (GameFullMessage) object;
			System.out.println(gfm.msg());
		}
		else if(object instanceof ConnectedMessage){
			ConnectedMessage msg = (ConnectedMessage) object;
			
			game.player(msg.player());
			
		}else if(object instanceof StartTurn){
			StartTurn msg = (StartTurn)object;
			game.queueVisibleCells(msg.visibleCells); //Updates the lighting data
			game.queueVisibleEnemies(msg.visibleEnemies);
			
			//if(msg.active.id() == game.player().id()){
			//	game.setActive(); //Tell the game its now our turn
			//}
		
		//When ConnectMessage is received we have been matched and a server has created our game instance
		}else if(object instanceof ConnectMessage){
			ConnectMessage msg = (ConnectMessage)object;
			Player player = new Player(connection);
			player.instanceId(msg.instanceId);
			player.team(msg.team);
			
			game.player(player);
			((ConnectState)game.state()).mapName(msg.mapName);
			
			//Send the message back to complete handshake
			connection.sendTCP(msg);
		}
	}

}
