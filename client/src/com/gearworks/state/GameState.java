package com.gearworks.state;


import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.gearworks.Game;
import com.gearworks.game.Entity;
import com.gearworks.game.Level;
import com.gearworks.game.Character;

public class GameState implements State {
	private static int ID = 0;
	

	@Override
	public void render(Game game) {
		game.level().render();
		
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
		game.level().load("assets/test.tmx");
		
		Character sneaker = (Character)game.spawn(new Character(game));
		sneaker.tile((int)game.level().getSneakerSpawn().x, (int)game.level().getSneakerSpawn().y);
		game.ui().activeCharacter(sneaker);
		
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
