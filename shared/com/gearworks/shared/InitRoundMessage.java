package com.gearworks.shared;

import com.gearworks.shared.Player.Team;

public class InitRoundMessage extends Message {

	public boolean active;
	public String mapName;
	public int round;
	public int turncount;
	public int movesLeft;
	public Team team;
	
}
