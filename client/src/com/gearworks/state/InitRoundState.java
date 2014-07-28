package com.gearworks.state;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gearworks.Game;
import com.gearworks.game.ClientLevel;
import com.gearworks.shared.InitRoundMessage;
import com.gearworks.shared.Player;

public class InitRoundState implements State {
	boolean received = false; 
	

	@Override
	public void render(Game game) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Game game) {
		if(game.messageQueue().peek() instanceof InitRoundMessage){
			InitRoundMessage msg = (InitRoundMessage)game.messageQueue().poll();
			game.turncount(msg.turncount);
			game.player().team(msg.team);
			if(msg.active){
				game.setActive();
			}else{
				game.setInactive();
			}
			game.round(msg.round);
			game.movesLeft(msg.movesLeft);
			game.level(new ClientLevel(game));
			game.level().load(msg.mapName);
			
			game.player().connection().sendTCP(msg); //Complete handshake
			
			received = true;
		}
		
		game.setState(new GameState());
	}

	@Override
	public void onEnter(Game game) {
		// Nothing just waiting for InitRoundMessage
	}

	@Override
	public void onExit(Game game) {
		//Setup dummy enemy
		game.enemy(new Player(null));
		if(game.player().team() == Player.Team.Seeker){
			game.enemy().team(Player.Team.Sneaker);
		}else{
			game.enemy().team(Player.Team.Seeker);			
		}
		game.enemy().spawnCharacters(game);
		
		game.player().spawnCharacters(game);
		
		//Calc initial lighting
		Array<Vector2> visibleCells = game.level().calculateLighting(game.player());
		Vector2[] visibleArray = visibleCells.toArray(Vector2.class);
		game.level().calculateHiddenCells(visibleArray);

	}

	@Override
	public boolean canEnterState(Game game) {
		return game.player().instanceId() != -1;
	}

	@Override
	public boolean canExitState(Game game) {
		return received;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 2;
	}

}
