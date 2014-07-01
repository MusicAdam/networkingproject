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
		
		if(instance.activePlayer() == null){
			instance.activePlayer(instance.players()[0]);
		}else{
			if(instance.activePlayer().equals(instance.players()[0])){
				instance.activePlayer(instance.players()[1]);
			}else{
				instance.activePlayer(instance.players()[0]);
			}
		}
		
		StartTurn msg = new StartTurn();

		for(Player pl : instance.players()){
			msg.active = (instance.activePlayer() == pl);
			msg.visibleCells = instance.level().calculateLighting(pl).toArray(Vector2.class);
			System.out.println("Visible cells: " + msg.visibleCells.length);
			msg.visibleEnemies = instance.level().calculateVisibleEnemies(pl, msg.visibleCells).toArray(Vector2.class);
			
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getId() {
		return ID;
	}

}
