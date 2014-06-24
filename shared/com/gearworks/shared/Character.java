package com.gearworks.shared;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.gearworks.Game;

public class Character extends Entity {
	Texture myTexture;
	Player player;
	int x, y;
	
	public Character(Player player, Game cRef) {
		super(cRef);
		this.player = player;
		myTexture = new Texture(Gdx.files.internal("assets/person.png"));
		size(32, 32);
	}
	
	//Sets myCell
	public void tile(int x, int y){
		this.x = x;
		this.y = y;
		
		Vector2 position = game.level().positionFromIndex(x, y);
		position(position);
	}
	
	//Gets myCell
	public Vector2 index(){
		return new Vector2(x, y);
	}
	
	public void move(int x, int y){
		if(game.level().isWall(x, y)) return;
		
		tile(x, y); //sets tile
		game.level().calculateLighting(player);
	}
	
	@Override
	public void render(SpriteBatch b, ShapeRenderer r){
		b.begin();
			b.draw(myTexture, position().x, position().y);
		b.end();
	}
	
}