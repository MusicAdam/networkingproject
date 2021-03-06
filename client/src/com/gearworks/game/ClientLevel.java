package com.gearworks.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gearworks.Game;
import com.gearworks.shared.Character;
import com.gearworks.shared.Entity;
import com.gearworks.shared.Player;

public class ClientLevel extends Level {

	public ClientLevel(Game game) {
		super(game);
	}
	

	
	public void visibleEnemies(Vector2[] enemies){
		System.out.println("[ClientLevel] Setting visible enemies:");
		System.out.println("\tEnemy Characters: " + game.enemy().characters().size);
		if(game.enemy().characters().size != 0){
			System.out.println("\tDeleting character");
			for(Character c : game.enemy().characters()){
				game.destroy(c);
			}
			game.enemy().clearCharacters();
			System.out.println("\tEnemy Characters (After prune): " + game.enemy().characters().size);
		}
			
		visibleEnemies = enemies;
		
		
		calculateVisibleEnemies();
	}
	
	public void calculateHiddenCells(Vector2[] visibleCells){
		hiddenCells = new Array<Vector2>();
		

		for(int x = 0; x < mapWidth; x++){
			for(int y = 0; y < mapHeight; y++){
				Vector2 cell = new Vector2(x, y);
				
				boolean contains = false;

				for(Vector2 visibleCell : visibleCells){
					if(visibleCell.equals(cell)){
						contains = true;
						break;
					}
				}
				
				if(!contains)
					hiddenCells.add(cell);
			}
		}
	}
	
	public void calculateVisibleEnemies(){
		//Also calculate the visible enemies here
		Array<Character> toRemove = new Array<Character>();
		for(Character c : game.enemy().characters()){
			if(hiddenCells.contains(c.index(), false)){
				toRemove.add(c);
				game.destroy(c);
			}
		}
		
		for(Character c : toRemove){
			game.enemy().removeCharacter(c);
		}
		
		for(Vector2 cell : visibleEnemies){
			if(!hiddenCells.contains(cell, false)){
				System.out.println("Spawning enemies");
				Character c = (Character)game.spawn(new Character(game.enemy(), game));
				c.tile((int)cell.x, (int)cell.y);
				game.enemy().addCharacter(c);
			}
		}
	}
}
