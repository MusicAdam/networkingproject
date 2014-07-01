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
import com.gearworks.game.ServerPlayer;
import com.gearworks.shared.Entity;
import com.gearworks.shared.Player;

//Server enters this state after it has been initialized
public class ReadyState implements State {
	private static int ID = 0;

	@Override
	public void render(Game game) {
	}

	@Override
	public void update(Game game) {
		if(game.idlePlayers().size % 2 == 0 && game.idlePlayers().size >= 2){
			ServerPlayer p1 = game.idlePlayers().pop();
			ServerPlayer p2 = game.idlePlayers().pop();
			game.createInstance(p1, p2);
		}
	}

	@Override
	public void onEnter(Game game) {		
		game.server().addListener(new ServerListener(game.server(), game)); //Start listening to clients
		
		System.out.println("[ReadyState::onEnter]");
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
