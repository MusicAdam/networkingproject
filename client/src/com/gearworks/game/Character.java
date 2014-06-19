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
		System.out.println(myTile.getProperties().get("x"));
		System.out.println(myTile.getProperties().get("y"));
		//needs to set the character in the center of the cell
		position(x,y);
	}
	
	//Gets myCell
	public TiledMapTile tile(){
		return myTile;
	}
	
	
} 