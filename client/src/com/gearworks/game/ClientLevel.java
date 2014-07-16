package com.gearworks.game;

import com.badlogic.gdx.math.Vector2;
import com.gearworks.Game;
import com.gearworks.shared.Character;
import com.gearworks.shared.Entity;
import com.gearworks.shared.Player;

public class ClientLevel extends Level {

	public ClientLevel(Game game) {
		super(game);
	}
	

	
	public void visibleEnemies(Vector2[] enemies){
		if(visibleEnemies != null){
			for(Vector2 cell : visibleEnemies){
				Character c = characterInCell(cell);
				game.destroy(c);
			}
		}
			
		visibleEnemies = enemies;

		Player enemy = new Player(null);//Create a fake player to own enemy characters
		if(game.player().team() == Player.Team.Seeker){
			enemy.team(Player.Team.Sneaker);
		}else{
			enemy.team(Player.Team.Seeker);			
		}
		
		for(Vector2 cell : visibleEnemies){
			if(!hiddenCells.contains(cell, false)){
				Character c = (Character)game.spawn(new Character(enemy, game));
				c.tile((int)cell.x, (int)cell.y); //Need to use tile here because we don't want to recalculate lighting for the enemy characters
			}
		}
	}

}
