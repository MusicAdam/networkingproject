package com.gearworks.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gearworks.Game;
import com.gearworks.shared.Character;
import com.gearworks.shared.Player;
import com.gearworks.game.Level;

public class ServerLevel extends Level {
	//Server side only
	public ServerLevel(Game game) {
		super(game);
	}

	public Array<Vector2> calculateVisibleEnemies(Player pl, Vector2[] visibleCells){
		Array<Vector2> enemies = new Array<Vector2>();
		Player other;
		System.out.println("Player inst id: " + pl.instanceId());
		Instance instance = game.getInstance(pl.instanceId());
		
		if(instance.players()[0].equals(pl)){
			other = instance.players()[1];
		}else{
			other = instance.players()[0];
		}
		
		for(Vector2 cell : visibleCells){
			for(Character c : other.characters()){
				if(c.index().equals(cell)){
					enemies.add(c.index());
				}
			}
		}
		
		return enemies;
	}
}
