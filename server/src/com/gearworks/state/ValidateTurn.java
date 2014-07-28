package com.gearworks.state;

import com.badlogic.gdx.math.Vector2;
import com.gearworks.Game;
import com.gearworks.game.Instance;
import com.gearworks.game.ServerCharacter;
import com.gearworks.game.ServerPlayer;
import com.gearworks.shared.Character;

public class ValidateTurn implements State{
	public Instance instance;
	private Vector2[] moves;
	
	public ValidateTurn(Instance instance, Vector2[] moves){
		this.instance = instance;
		this.moves = moves;
	}

	@Override
	public void render(Game game) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Game game) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onEnter(Game game) {
		ServerPlayer pl = instance.activePlayer();
		
		int i = 0;
		for(ServerCharacter c : pl.serverCharacters()){
			if(i < moves.length && moves[i] != null)
				c.tile((int)moves[i].x, (int)moves[i].y);
			i++;
		}
		
		//ServerPlayer winner = game.checkVictory(this);
		
		//Toggle active player
		if(instance.activePlayer().equals(instance.players()[0])){
			instance.activePlayer(instance.players()[1]);
		}else{
			instance.activePlayer(instance.players()[0]);
		}
		
		instance.sm().setState(new PlayerTurn(instance));
	}

	@Override
	public void onExit(Game game) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean canEnterState(Game game) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canExitState(Game game) {
		return true;
	}

	@Override
	public int getId() {
		return 3;
	}

}
