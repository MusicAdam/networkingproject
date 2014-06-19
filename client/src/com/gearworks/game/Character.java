package com.gearworks.game;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.gearworks.Game;

public class Character extends Entity {
	
	public Character(Game cRef) {
		super(cRef);
		// TODO Auto-generated constructor stub
	}

	TiledMapTileLayer.Cell myCell; 
	//Sets myCell
	public void cell(TiledMapTileLayer.Cell cell){
		myCell = cell;
		float x=0;
		float y=0;
		System.out.println(myCell.getTile().getProperties().containsKey("x"));
		//needs to set the character in the center of the cell
		position(x,y);
	}
	
	//Gets myCell
	public TiledMapTileLayer.Cell cell(){
		return myCell;
	}
	
	
} 