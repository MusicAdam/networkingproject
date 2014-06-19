package com.gearworks.game;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
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
	protected Array<TiledMapTile> seekerSpawns;
	protected TiledMapTile sneakerSpawn;
	
	public Level(Game game){
		this.game = game;
	}
	
	public void load(String name){
		tileMap = new TmxMapLoader().load(name);
		mapRenderer = new OrthogonalTiledMapRenderer(tileMap);
		
		updateTilePositions();
		
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
	
	protected Array<TiledMapTile> findSeekerSpawns(){
		Array<TiledMapTile> spawns = new Array<TiledMapTile>();

		TiledMapTileLayer layer;
		if((layer = (TiledMapTileLayer) tileMap.getLayers().get(MAP_LAYER)) != null){
			for(int x = 0; x < layer.getWidth(); x++){
				for(int y = 0; y < layer.getHeight(); y++){
					TiledMapTileLayer.Cell cell = layer.getCell(x, y);
					if(cell.getTile().getProperties().containsKey("seekerSpawn")){
						spawns.add(cell.getTile());
					}
				}
			}
		}
		
		return spawns;
	}
	
	protected TiledMapTile findSneakerSpawn(){
		TiledMapTileLayer layer;
		if((layer = (TiledMapTileLayer) tileMap.getLayers().get(MAP_LAYER)) != null){
			for(int x = 0; x < layer.getWidth(); x++){
				for(int y = 0; y < layer.getHeight(); y++){
					TiledMapTileLayer.Cell cell = layer.getCell(x, y);
					if(cell.getTile().getProperties().containsKey("sneakerSpawn")){
						return cell.getTile();
					}
				}
			}
		}
		
		return null;
	}
	
	//Calculates each tile's position in each tile layer and sets an x & y property containing the value
	protected void updateTilePositions(){
		for(MapLayer mapLayer : tileMap.getLayers()){
			if(mapLayer instanceof TiledMapTileLayer){
				TiledMapTileLayer tileLayer = (TiledMapTileLayer)mapLayer;
				
				for(int x = 0; x < tileLayer.getWidth(); x++){
					for(int y = 0; y < tileLayer.getHeight(); y++){
						tileLayer.getCell(x, y).getTile().getProperties().put("x", x * tileLayer.getTileWidth() + tileLayer.getTileWidth()/2);
						tileLayer.getCell(x, y).getTile().getProperties().put("y", y * tileLayer.getTileHeight() + tileLayer.getTileHeight()/2);
					}
				}
			}
		}
	}
	
	public Array<TiledMapTile> getSeekerSpawns(){
		return seekerSpawns;
	}
	
	public TiledMapTile getSneakerSpawn(){
		return sneakerSpawn;
	}
	
	public void dispose(){
		tileMap.dispose();
		mapRenderer.dispose();
	}
	
}
