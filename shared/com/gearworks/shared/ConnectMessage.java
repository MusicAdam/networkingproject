package com.gearworks.shared;

/*
 * Interrobang = ? + !
 * 
 * This is sent by the server to tell the client a game instance has been created 
 */

public class ConnectMessage extends Message {
	
	public int 			instanceId;		//Server instance id
	public String 		mapName;		//The id of the map being played on
	public Player.Team	team;			//The team which the client will start on

}
