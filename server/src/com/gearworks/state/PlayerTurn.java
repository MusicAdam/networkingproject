package com.gearworks.state;

import com.badlogic.gdx.math.Vector2;
import com.gearworks.Game;
import com.gearworks.game.Instance;
import com.gearworks.game.ServerLevel;
import com.gearworks.shared.Player;
import com.gearworks.shared.StartTurn;

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
		System.out.println("[PlayerTurn::onEnter]");
		
		//If its the first turn make sure one of the players is active
		if(instance.activePlayer() == null){
			instance.activePlayer(instance.players()[0]);
		}
		
		StartTurn msg = new StartTurn();

		for(Player pl : instance.players()){
			msg.active = (instance.activePlayer() == pl);
			msg.visibleEnemies = instance.level().calculateVisibleEnemies(pl, (Vector2[])instance.level().calculateLighting(pl).toArray(Vector2.class)).toArray(Vector2.class);
			
			pl.connection().sendTCP(msg);
		}
	}

	@Override
	public void onExit(Game game) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean canEnterState(Game game) {
		return true;
	}

	@Override
	public boolean canExitState(Game game) {
		return true;
	}

	@Override
	public int getId() {
		return ID;
	}

}
