package com.gearworks.state;

import com.gearworks.Game;
import com.gearworks.game.Instance;

public class PlayerTurn implements State {
	private static int ID = 2;
	
	private Instance instance;
	
	public PlayerTurn(Instance instance){
		this.instance = instance;
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
		// TODO Auto-generated method stub

	}

	@Override
	public void onExit(Game game) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean canEnterState(Game game) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canExitState(Game game) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getId() {
		// TODO Auto-generated method stub
		return 0;
	}

}
