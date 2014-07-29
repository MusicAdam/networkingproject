package com.gearworks.shared;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gearworks.Game;
import com.gearworks.shared.Player.Team;

public class Character extends Entity {
	public static final int SNEAKER_RADIUS = 5;
	public static final int NUM_MOVES = 5;
	
	private Texture myTexture;
	private Sprite mySprite;
	private Player player;
	protected int x, y;
	private int moves_left;
	
	public Character(Player player, Game cRef) {
		super(cRef);
		this.player = player;
		if(player.team() == Player.Team.Seeker){
			myTexture = new Texture(Gdx.files.internal("assets/seekerv2.png"));
		}else{
			myTexture = new Texture(Gdx.files.internal("assets/sneaker.png"));
		}
		
		mySprite = new Sprite(myTexture, 32, 32);
		size(32, 32);
		moves_left = NUM_MOVES;
	}
	
	//Sets myCell
	public void tile(int x, int y){
		this.x = x;
		this.y = y;
		
		Vector2 position = game.level().positionFromIndex(x, y);
		position(position);
		mySprite.setPosition(position().x, position().y);
	}
	
	//Gets myCell
	public Vector2 index(){
		return new Vector2(x, y);
	}
	
	public void move(int x, int y){
		if(game.level().isWall(x, y)) return;
		
		if(moves_left > 0){
			//Get current x and y
			int dx = x - this.x;
			int dy = y - this.y;
			
			if(dx < 1 && dy == 0){
				if(!mySprite.isFlipX())
					mySprite.flip(true, false);
			}else if(dy == 0){
				if(mySprite.isFlipX())
					mySprite.flip(true, false);			
			}
			
			tile(x, y); //sets tile
			
			//Need to continue calculating lighting as the player moves around the map
			Array<Vector2> visibleCells = game.level().calculateLighting(player);
			Vector2[] visibleArray = visibleCells.toArray(Vector2.class);
			game.level().calculateHiddenCells(visibleArray);							//fake error, would throw if this was ever called server-side
			game.level().calculateVisibleEnemies();										//same^
			moves_left--;
		}
		else{
			moves_left = NUM_MOVES;
			game.sendEndTurnMessage();													//hopefully the same type of non-error error
		}
	}
	
	
	
	@Override
	public void render(SpriteBatch b, ShapeRenderer r){
		b.begin();
			mySprite.draw(b);
			//b.draw(myTexture, position().x, position().y);
		b.end();
	}
	
}