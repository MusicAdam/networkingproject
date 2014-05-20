package com.gearworks.state;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gearworks.Client;
import com.gearworks.game.Entity;

public interface State {
	public void render(Client game);
	public void update(Client game);
	public void onEnter(Client game);
	public void onExit(Client game);
	public boolean canEnterState(Client game);
	public boolean canExitState(Client game);
	public int getId();
}
