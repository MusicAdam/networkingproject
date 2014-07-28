package com.gearworks.state;

import com.gearworks.Game;
import com.gearworks.game.Instance;
import com.gearworks.game.ServerLevel;
import com.gearworks.game.ServerPlayer;
import com.gearworks.shared.InitRoundMessage;
import com.gearworks.shared.Player.Team;

//Initializes the round server side:
//	-Set instance turncount = 0;
//  -Set instance movesLeft = active team's moves
//  -Set active player based on which team goes first
//  -Set level
//  -Send this info to the client
public class InitRoundState implements State {
	
	Instance instance;
	int round;
	
	public InitRoundState(Instance inst, int round){
		instance = inst;
		this.round = round;
	}

	@Override
	public void render(Game game) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Game game) {
		instance.sm().setState(new PlayerTurn(instance));
	}

	@Override
	public void onEnter(Game game) {
		//Swap teams
		if(instance.players()[0].team() == Team.Sneaker){
			instance.players()[0].team(Team.Seeker);
			instance.players()[1].team(Team.Sneaker);
		}else{
			instance.players()[1].team(Team.Seeker);
			instance.players()[0].team(Team.Sneaker);
		}
		
		instance.turncount(0);
		
		if(Instance.STARTING_TEAM == Team.Sneaker){
			instance.movesLeft(Instance.SNEAKER_MOVES);
		}else{
			instance.movesLeft(Instance.SEEKER_MOVES);			
		}
		
		instance.activePlayer(instance.getPlayerByTeam(Instance.STARTING_TEAM));
		instance.mapName("assets/map" + round + ".tmx");
		
		
		InitRoundMessage msg = new InitRoundMessage();
		msg.turncount	= instance.turncount();
		msg.mapName 	= instance.mapName();
		msg.round = round;
		
		for(ServerPlayer pl : instance.players()){
			msg.active 		= (instance.activePlayer() == pl);
			msg.movesLeft	= (msg.active) ? instance.movesLeft() : 0;
			msg.team = pl.team();
			
			pl.connection().sendTCP(msg);
		}
		

		System.out.println("[InitRoundState::onEnter]");
	}

	@Override
	public void onExit(Game game) {
		
		System.out.println("ON EXIT WAS CALLED!");
		
		instance.level(new ServerLevel(game));
		instance.level().load(instance.mapName());

		//Create the characters server side
		instance.spawnPlayerCharacters();
	}

	@Override
	public boolean canEnterState(Game game) {
		return instance.clientsConnected();
	}

	@Override
	public boolean canExitState(Game game) {
		return instance.clientsReady();
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 4;
	}

}
