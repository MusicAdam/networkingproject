package com.gearworks.state;

import com.badlogic.gdx.math.Vector2;
import com.gearworks.Game;
import com.gearworks.game.Instance;
import com.gearworks.game.ServerPlayer;
import com.gearworks.shared.Character;

public class ProcessTurn implements State{
	public Instance instance;
	private Vector2[] moves;
	
	public ProcessTurn(Instance instance, Vector2[] moves){
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
		for(Character c : pl.characters()){
			if(moves[i] != null)
				c.move((int)moves[i].x, (int)moves[i].y);
			i++;
		}
		
		//ServerPlayer winner = game.checkVictory(this);
		
		//Toggle active player
		if(instance.activePlayer().equals(instance.players()[0])){
			instance.activePlayer(instance.players()[1]);
		}else{
			instance.activePlayer(instance.players()[0]);
		}
		
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
