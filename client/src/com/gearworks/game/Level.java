package com.gearworks.game;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Array;
import com.gearworks.Game;

public class Level {
	public static final String MAP_LAYER = "map";
	
	private TiledMap tileMap;
	private OrthogonalTiledMapRenderer mapRenderer;
	private Game game;
	protected Array<TiledMapTileLayer.Cell> seekerSpawns;
	protected TiledMapTileLayer.Cell sneakerSpawn;
	
	public Level(Game game){
		this.game = game;
	}
	
	public void load(String name){
		tileMap = new TmxMapLoader().load(name);
		mapRenderer = new OrthogonalTiledMapRenderer(tileMap);
		
		seekerSpawns = findSeekerSpawns();
		sneakerSpawn = findSneakerSpawn();
	}

	public void render() {
		if(mapRenderer == null) return;
				
		mapRenderer.setView(game.camera());
		mapRenderer.render();
	}

	public void update() {
	}
	
	public TiledMapTileLayer.Cell getCell(String layerName, int x, int y){
		TiledMapTileLayer layer;
		if((layer = (TiledMapTileLayer) tileMap.getLayers().get(layerName)) != null){
			return layer.getCell(x, y);
		}
		
		return null;
	}
	
	protected Array<TiledMapTileLayer.Cell> findSeekerSpawns(){
		Array<TiledMapTileLayer.Cell> spawns = new Array<TiledMapTileLayer.Cell>();

		TiledMapTileLayer layer;
		if((layer = (TiledMapTileLayer) tileMap.getLayers().get(MAP_LAYER)) != null){
			for(int x = 0; x < layer.getWidth(); x++){
				for(int y = 0; y < layer.getHeight(); y++){
					TiledMapTileLayer.Cell cell = layer.getCell(x, y);
					if(cell.getTile().getProperties().containsKey("seekerSpawn")){
						spawns.add(cell);
					}
				}
			}
		}
		
		return spawns;
	}
	
	protected TiledMapTileLayer.Cell findSneakerSpawn(){
		TiledMapTileLayer layer;
		if((layer = (TiledMapTileLayer) tileMap.getLayers().get(MAP_LAYER)) != null){
			for(int x = 0; x < layer.getWidth(); x++){
				for(int y = 0; y < layer.getHeight(); y++){
					TiledMapTileLayer.Cell cell = layer.getCell(x, y);
					if(cell.getTile().getProperties().containsKey("sneakerSpawn")){
						return cell;
					}
				}
			}
		}
		
		return null;
	}
	
	public Array<TiledMapTileLayer.Cell> getSeekerSpawns(){
		return seekerSpawns;
	}
	
	public TiledMapTileLayer.Cell getSneakerSpawn(){
		return sneakerSpawn;
	}
	
	public void dispose(){
		tileMap.dispose();
		mapRenderer.dispose();
	}
	
}
