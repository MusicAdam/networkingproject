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
import com.gearworks.shared.ConnectMessage;
import com.gearworks.shared.Entity;
import com.gearworks.shared.Player;

//Server enters this state after it has been initialized
//State is responsible for ensuring players are properly connected.
public class InstanceInitState implements State {
	private static int ID = 1;
	private Instance  instance;
	
	public InstanceInitState(Instance inst){
		instance = inst;
	}

	@Override
	public void render(Game game) {
	}

	@Override
	public void update(Game game) {
		System.out.println("Calling:");
		instance.sm().setState(new InitRoundState(instance, 1));
	}

	@Override
	public void onEnter(Game game) {
		//Generate connect messages for client
		ConnectMessage connectMessage = new ConnectMessage();
		connectMessage.instanceId = instance.id();
		
		instance.players()[0].team(Player.Team.Sneaker);
		instance.players()[0].connection().sendTCP(connectMessage);

		instance.players()[1].team(Player.Team.Seeker);
		instance.players()[1].connection().sendTCP(connectMessage);
		
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
		return instance.clientsConnected();
	}
	
	@Override
	public int getId(){ return ID; }

}
