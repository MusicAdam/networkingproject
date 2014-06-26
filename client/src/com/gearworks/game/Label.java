package com.gearworks.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.gearworks.Game;
import com.gearworks.shared.Utils;

public class Label extends Gui{
	public Label(Game cRef) {
		super(cRef);
		// TODO Auto-generated constructor stub
	}

	int x, y;
	String myString;
	
	@Override
	public void render(SpriteBatch B, ShapeRenderer r){
		Utils.drawRect(r, Color.BLUE, 20, 20, 100, 100);
		
	}
}