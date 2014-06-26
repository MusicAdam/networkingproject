package com.gearworks.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Array;
import com.gearworks.Game;
import com.gearworks.shared.Entity;
import com.gearworks.shared.Utils;

public class GuiElement extends Entity{
	private Color fillColor;
	private Color borderColor;
	private Texture myTexture;
	private float borderSize;
	private float padding;
	private float margin;
	
	public GuiElement(Game cRef) {
		super(cRef);
		
		//Defaults
		size(100, 100);
		fillColor = new Color(1, 1, 1, .5f);
		borderColor = new Color(1, 0, 0, .5f);
		borderSize = 10;
	}

	//private Array<Gui> stuff;

	public void render(SpriteBatch b, ShapeRenderer r) {
		float x = position().x + game.camera().position.x - size().x/2;
		float y = position().y + game.camera().position.y - size().y/2;
	

		Gdx.gl.glEnable(GL20.GL_BLEND); //Need to enable blending for alpha rendering
		r.identity();
		r.setProjectionMatrix(game.camera().combined);
		
		r.begin(ShapeType.Filled);
			r.setColor(fillColor);
			r.scale(game.camera().zoom, game.camera().zoom, 0);
			r.translate(x, y, 0f);
			r.rect(0, 0, size().x, size().y);
		r.end();
		
		//Need to handle drawing the borders ourselfs because glLineWidth is not well supported
		//also libgdx's ShapreRenderer.rect isn't very good.
		
		//Draw the left border
		float bx = x - borderSize;
		float by = y;

		Utils.fillRect(r, borderColor, bx, by - borderSize, borderSize, size().y + borderSize * 2);
		
		//Draw Right border
		bx = x + size().x;
		
		Utils.fillRect(r, borderColor, bx, by - borderSize, borderSize, size().y  + borderSize * 2); //We add on the border size to the height here so there is not gap at the top/bottom
		
		//Draw top border
		bx = x;
		by = y + size().y;
		
		Utils.fillRect(r, borderColor, bx, by, size().x, borderSize);
		
		//Draw bottom border
		by = y - borderSize;
		
		Utils.fillRect(r, borderColor, bx, by, size().x, borderSize);
		
		Gdx.gl.glDisable(GL20.GL_BLEND); //Need to enable blending for alpha rendering
		
		if(myTexture != null){
			b.begin();
			b.draw(myTexture, x, y);
			b.end();
		} 
	} 
	
	public void fillColor(Color c){ fillColor = c; }
	public Color fillColor(){ return fillColor; }
}