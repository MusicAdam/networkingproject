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

	
	public ServerListener(Server s, Game g){
		game = g;
		server = s;

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
		game.queueRemovePlayer(pl);
	}
	
	@Override
	 public void received (Connection connection, Object object) {
		
		if(object instanceof ConnectMessage){
			ConnectMessage msg = (ConnectMessage)object;
			
			System.out.println("Attempting to get instance: " + msg.instanceId);
			Instance instance = game.getInstance(msg.instanceId);
			Player pl = instance.getPlayerByConnection(connection);
			pl.instanceId(msg.instanceId);
			System.out.println("Player inst in list: " + pl.instanceId());
		}
		
		if (object instanceof EndTurn){
			
			EndTurn endt = (EndTurn)object;
			game.addToMessageQueue(endt);
			
		}
	}
}
