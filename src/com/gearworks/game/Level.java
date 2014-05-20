package com.gearworks.game;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.gearworks.Client;

public class Level {
	private TiledMap tileMap;
	private OrthogonalTiledMapRenderer mapRenderer;
	private Client game;
	
	public Level(Client game){
		this.game = game;
	}
	
	public void load(String name){
		tileMap = new TmxMapLoader().load(name);
		mapRenderer = new OrthogonalTiledMapRenderer(tileMap);
	}
	
	

	public void render() {
		if(mapRenderer == null) return;
				
		mapRenderer.setView(game.camera());
		mapRenderer.render();
	}

	public void update() {
	}
	
}
