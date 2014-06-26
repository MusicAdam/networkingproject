package com.gearworks.gui;

import javax.swing.plaf.nimbus.State;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gearworks.Game;

public class MenuState extends State{
	
	private SpriteBatch b;
	private BitmapFont titleFont;
	private BitmapFont font;
	
	private final String title="Seek'n'Sneak";
	
	private int currentItem;
	private String[] menuItems;
	
	public void onEnter(){
		b = new SpriteBatch();
		
		FreeTypeFontGeneretor gen= new FreeTypeFontGenereator(Gdx.files.internal("fonts/"));
		
		titleFont = gen.generateFont(56);
		titleFont.setColor(Color.WHITE);
		
		font = gen.generateFont(20);
		
		menuItems = new String[]{
				"Play",
				"Options",
				"Exit",
				"Credits"
		};
	}
	
	public void update(){
		
	}
	
	public MenuState(Game cRef) {
		super(cRef);
		// TODO Auto-generated constructor stub
	}

}
