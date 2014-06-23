package com.gearworks;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.gearworks.shared.*;


public class ServerListener extends Listener{
	
	private Server server;
	
	public ServerListener(Server s){
		
		server = s;
	}
	
	@Override
	 public void received (Connection connection, Object object) {
		if(object instanceof ConnectMessage){
			//do nothing? because TCP handles the connecting
		}
	}
}
