package com.gearworks.game;

import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.gearworks.Game;

public class Character extends Entity {
	
	public Character(Game cRef) {
		super(cRef);
		// TODO Auto-generated constructor stub
	}

	TiledMapTile myTile; 
	//Sets myCell
	public void tile(TiledMapTile tile){
		myTile = tile;
		float x=0;
		float y=0;
		x= (float)myTile.getProperties().get("x");
		y= (float)myTile.getProperties().get("y");
		position(x,y);
	}
	
	//Gets myCell
	public TiledMapTile tile(){
		return myTile;
	}
	
	public void move(int x, int y){
		TiledMapTile tile;
		tile = game.level().getCell(Level.MAP_LAYER,x,y).getTile();
		tile(tile);
	}
	
}