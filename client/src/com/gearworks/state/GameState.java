package com.gearworks.state;


import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.gearworks.Game;
import com.gearworks.shared.Entity;
import com.gearworks.shared.Level;
import com.gearworks.shared.Player;
import com.gearworks.shared.Character;
import com.gearworks.shared.Level;

/*
 * This is the state the player is in after connection has been made.
 */
public class GameState implements State {
	private static int ID = 0;
	

	@Override
	public void render(Game game) {		
		game.level().render(game.renderer());
		
		for(Entity ent : game.entities()){
			ent.render(game.batch(), game.renderer());
		}
		
	}

	@Override
	public void update(Game game) {
		for(Entity ent : game.entities()){
			ent.update();
		}
	}
	
	@Override
	public void onEnter(Game game) {		
		game.level(new Level(game));
		game.level().load("assets/map2.tmx");
		
		Player pl = new Player(0, Player.Team.Sneaker);
		pl.spawnCharacters(game);
		game.ui().activeCharacter(pl.characters().first());
		game.player(pl);
		
		game.level().calculateLighting(pl);
		
		System.out.println("[GameState::onEnter]");
	}

	@Override
	public void onExit(Game game) {
		game.level().dispose();
		
	}
	
	@Override
	public boolean canEnterState(Game game) {
		return true;
	}

	@Override
	public boolean canExitState(Game game) {
		return false;
	}
	
	@Override
	public int getId(){ return ID; }


}
