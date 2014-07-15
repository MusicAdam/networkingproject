package com.gearworks;


import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.gearworks.shared.Character;
import com.gearworks.shared.Entity;
import com.gearworks.state.GameState;

public class UserInterface implements InputProcessor{
	public static final float MAX_ZOOM = 1.2f;
	public static final float MIN_ZOOM = .08f;
	private Game game;
	
	private Character activeCharacter;
	private InputMapper inputMapper;
	
	float selectionPadding = 0f;
	
	//Takes a mouse coordinate and returns screen coordinates, does not alter original vector
	public static Vector2 mouseToScreen(Vector2 coord, Camera camera){
		Vector3 screenCoord = new Vector3(coord.x, coord.y, 0);
		camera.unproject(screenCoord);
		
		return new Vector2(screenCoord.x, screenCoord.y);
	}
	
	public UserInterface(Game game){
		this.game = game;
		inputMapper = new InputMapper();
		
		//Default mappings
		inputMapper.put("up", Input.Keys.W);
		inputMapper.put("up", Input.Keys.UP);
		inputMapper.put("left", Input.Keys.A);
		inputMapper.put("left", Input.Keys.LEFT);
		inputMapper.put("right", Input.Keys.D);
		inputMapper.put("right", Input.Keys.RIGHT);
		inputMapper.put("down", Input.Keys.DOWN);
		inputMapper.put("down", Input.Keys.S);
		inputMapper.put("cycle", Input.Keys.TAB);
	}

	@Override
	public boolean keyDown(int keycode) {
		if(inputMapper.getMapping(keycode) == "up"){
			if(activeCharacter != null){
				Vector2 index = game.level().indexFromPosition(activeCharacter.position());
				activeCharacter.move((int)index.x, (int)index.y + 1);
			}
		}
		
		if(inputMapper.getMapping(keycode) == "left"){
			if(activeCharacter != null){
				Vector2 index = game.level().indexFromPosition(activeCharacter.position());
				activeCharacter.move((int)index.x - 1, (int)index.y);
			}
		}
		
		if(inputMapper.getMapping(keycode) == "down"){
			if(activeCharacter != null){
				Vector2 index = game.level().indexFromPosition(activeCharacter.position());
				activeCharacter.move((int)index.x, (int)index.y - 1);
			}
		}
		
		if(inputMapper.getMapping(keycode) == "right"){
			if(activeCharacter != null){
				Vector2 index = game.level().indexFromPosition(activeCharacter.position());
				activeCharacter.move((int)index.x + 1, (int)index.y);
			}
		}
		
		if(inputMapper.getMapping(keycode) == "cycle"){
			if(activeCharacter == null){
				activeCharacter = game.player().characters().first();
			}else{
				int index = -1; //Will be set to the index of the active character
				
				for(int i = 0; i < game.player().characters().size; i++){
					if(game.player().characters().get(i).equals(activeCharacter)){
						index = i + 1; //Use the next index so we get the next character in the array
						break;
					}
				}
				
				if(index == -1 || index >= game.player().characters().size){ //If index is out of bounds
					activeCharacter = game.player().characters().first(); //Default to first character
				}else{
					activeCharacter = game.player().characters().get(index); //Get the index we found
				}
			}
		}
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false; 
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false; 
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return true;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		float zoom = game.camera().zoom + .1f * amount;
		if(zoom <= MAX_ZOOM && zoom >= MIN_ZOOM)
			game.camera().zoom = zoom;
		return false;
	}
	
	public void render(SpriteBatch batch, ShapeRenderer renderer){
		renderer.setProjectionMatrix(game.camera().combined);
		renderer.identity();
	}
	
	public void activeCharacter(Character c){ activeCharacter = c; }
	public Character activeCharacter(){ return activeCharacter; }

}
