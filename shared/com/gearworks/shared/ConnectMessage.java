package com.gearworks.shared;

/*
 * Interrobang = ? + !
 * 
 * This is sent by the server to tell the client a game instance has been created 
 */

public class ConnectMessage extends Message {
	
	public int 			instanceId;		//Server instance id

}
