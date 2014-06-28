package com.gearworks.state;


import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.gearworks.Game;
import com.gearworks.ServerListener;
import com.gearworks.game.Instance;
import com.gearworks.shared.Entity;

//Server enters this state after it has been initialized
public class InstanceInitState implements State {
	private static int ID = 0;
	private Instance  instance;
	
	public InstanceInitState(Instance inst){
		instance = inst;
	}

	@Override
	public void render(Game game) {
	}

	@Override
	public void update(Game game) {
	}

	@Override
	public void onEnter(Game game) {
		//Generate connect messages for client
		
		
		
		System.out.println("[InstanceInitState::onEnter]");
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
		return instance.clientsReady();
	}
	
	@Override
	public int getId(){ return ID; }

}
