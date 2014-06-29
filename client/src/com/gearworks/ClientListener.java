package com.gearworks;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.gearworks.shared.*;

public class ClientListener extends Listener{
	
	private Game game;
	private Client client;
	
	public ClientListener(Game g, Client c){
		
		game = g;
		client = c;
		
	}
	
	public void received (Connection connection, Object object){
		if(object instanceof GameFullMessage){
			//SOMEHOW DISPLAY IT TO THE FUCKERS
			GameFullMessage gfm = (GameFullMessage) object;
			System.out.println(gfm.msg());
		}
		else if(object instanceof ConnectedMessage){
			ConnectedMessage msg = (ConnectedMessage) object;
			
			game.player(msg.player());
			
		}else if(object instanceof StartTurn){
			StartTurn msg = (StartTurn)object;
			
			//level.updateHiddenCells(msg.hiddenCells); //Updates the lighting data
			//level.updateEnemies(msg.visibleEnemies); //Updates which enemies are visible
			
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
			game.level().load(msg.mapName);
		}
	}

}
