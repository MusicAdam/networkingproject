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
			Character c = (Character)game.spawn(new Character(enemy, game));
			c.move((int)cell.x, (int)cell.y);
		}
	}
	
	public Character characterInCell(Vector2 cell){
		for(Entity ent : game.entities()){
			if(ent instanceof Character){
				Character c = (Character)ent;
				
				if(c.index().equals(cell)){
					return c;
				}
			}
		}
		
		return null;
	}

}
