package com.gearworks.state;


import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gearworks.Game;
import com.gearworks.shared.Entity;
import com.gearworks.shared.Player;

public class GameState implements State {
	private static int ID = 0;

	@Override
	public void render(Game game) {
		game.level().render(game.renderer());
		
		game.batch().setProjectionMatrix(game.camera().combined);
		
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
		System.out.println("[GameState::onEnter]");
	}

	@Override
	public void onExit(Game game) {
		
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
	public int getId(){ return ID; }

}
