package com.gearworks.state;


import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.gearworks.Client;
import com.gearworks.game.Entity;

public class GameState implements State {
	private static int ID = 0;

	@Override
	public void render(Client game) {
		game.batch().setProjectionMatrix(game.camera().combined);
		game.batch().begin();
		for(Entity ent : game.entities()){
			ent.render(game.batch(), game.renderer());
		}
		game.batch().end();
	}

	@Override
	public void update(Client game) {
		for(Entity ent : game.entities()){
			ent.update();
		}
	}

	@Override
	public void onEnter(Client game) {		
		System.out.println("[GameState::onEnter]");
	}

	@Override
	public void onExit(Client game) {
		
	}
	
	@Override
	public boolean canEnterState(Client game) {
		return true;
	}

	@Override
	public boolean canExitState(Client game) {
		return false;
	}
	
	@Override
	public int getId(){ return ID; }

}
