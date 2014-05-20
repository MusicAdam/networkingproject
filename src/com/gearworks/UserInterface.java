package com.gearworks;


import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.gearworks.game.Entity;
import com.gearworks.state.GameState;

public class UserInterface implements InputProcessor{
	private Client game;
	
	private ArrayList<Entity> selected;
	private Vector2 dragStart;
	private Vector2 dragPos;
	
	float selectionPadding = 0f;
	
	//Takes a mouse coordinate and returns screen coordinates, does not alter original vector
	public static Vector2 mouseToScreen(Vector2 coord){
		Vector2 screenCoord = coord.cpy();
		screenCoord.x = coord.x;
		screenCoord.y = Math.abs(coord.y - (Client.V_HEIGHT * Client.SCALE));
		return screenCoord;
	}
	
	public UserInterface(Client game){
		this.game = game;
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
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
		if(button == 0){
			selected = null;
		}
		return true; //This could interfere with menus in the future, unless this class handles the clicks...
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(dragStart != null && dragPos != null){
			//If the height is negative we want to swap dragStart.y and dragPos.y to flip the box so it is not upside down and picking will work
			if(dragPos.y - dragStart.y < 0){
				float tmp = dragPos.y;
				dragPos.y = dragStart.y;
				dragStart.y = tmp;
			}
			
			//Do this for x axis as well
			if(dragPos.x - dragStart.x < 0){
				float tmp = dragPos.x;
				dragPos.x = dragStart.x;
				dragStart.x = tmp;
			}
			
			Rectangle bounds = new Rectangle(	dragStart.x,
												dragStart.y,
												dragPos.x - dragStart.x,
												dragPos.y - dragStart.y);
			selected = Utils.selectEntitiesInBox(game.entities(), bounds);
			
			dragStart = null;
			dragPos = null;
		}
		return true; //This could interfere with menus in the future, unless this class handles the clicks...
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if(Gdx.input.isButtonPressed(Input.Buttons.LEFT)){
			if(dragStart == null)
				dragStart = mouseToScreen(new Vector2(screenX, screenY));
			
			dragPos = mouseToScreen(new Vector2(screenX, screenY));
		}
		
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
		
		//Draw the selection box
		if(dragStart != null && dragPos != null){
			float width = dragPos.x - dragStart.x;
			float height = dragPos.y - dragStart.y;

			Utils.drawRect(renderer, Color.RED, dragStart.x, dragStart.y, width, height);
		}
	}

}
