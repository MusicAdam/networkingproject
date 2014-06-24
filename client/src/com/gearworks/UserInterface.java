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
import com.gearworks.game.Entity;
import com.gearworks.state.GameState;
import com.gearworks.game.Character;

public class UserInterface implements InputProcessor{
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
		inputMapper.put("switchActive", Input.Keys.TAB);
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
		Vector2 coord = mouseToScreen(new Vector2(screenX, screenY), game.camera());
		return true; //This could interfere with menus in the future, unless this class handles the clicks...
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return true; //This could interfere with menus in the future, unless this class handles the clicks...
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
		return false;
	}
	
	public void render(SpriteBatch batch, ShapeRenderer renderer){
		renderer.setProjectionMatrix(game.camera().combined);
		renderer.identity();
	}
	
	public void activeCharacter(Character c){ activeCharacter = c; }

}
