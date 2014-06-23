package com.gearworks.game;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gearworks.Game;

public class Level {
	public static final String MAP_LAYER = "map";
	
	private TiledMap tileMap;
	private OrthogonalTiledMapRenderer mapRenderer;
	private Game game;
	protected Array<TiledMapTile> seekerSpawns;
	protected Vector2 sneakerSpawn;
	
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
	
	public Vector2 positionFromIndex(int x, int y){
		TiledMapTileLayer layer;

		if((layer = (TiledMapTileLayer) tileMap.getLayers().get(Level.MAP_LAYER)) != null){
			return new Vector2(	x * layer.getTileWidth(),
								y * layer.getTileHeight());
		}
		
		return null;
	}
	
	public Vector2 indexFromPosition(Vector2 coord){
		TiledMapTileLayer layer;

		if((layer = (TiledMapTileLayer) tileMap.getLayers().get(Level.MAP_LAYER)) != null){
			int x = (int) Math.floor(coord.x / layer.getTileWidth());
			int y = (int) Math.floor(coord.y / layer.getTileHeight());
			return new Vector2(x, y);
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
	
	protected Vector2 findSneakerSpawn(){
		TiledMapTileLayer layer;
		if((layer = (TiledMapTileLayer) tileMap.getLayers().get(MAP_LAYER)) != null){
			for(int x = 0; x < layer.getWidth(); x++){
				for(int y = 0; y < layer.getHeight(); y++){
					TiledMapTileLayer.Cell cell = layer.getCell(x, y);
					if(cell.getTile().getProperties().containsKey("sneakerSpawn")){
						return new Vector2(x, y);
					}
				}
			}
		}
		
		return null;
	}
	
	//Calculates each tile's position in each tile layer and sets an x & y property containing the value
	protected void updateTilePosition(TiledMapTile tile, TiledMapTileLayer tileLayer, int x, int y){
		tile.getProperties().put("x", x * tileLayer.getTileWidth() + tileLayer.getTileWidth()/2);
		tile.getProperties().put("y", y * tileLayer.getTileHeight() + tileLayer.getTileHeight()/2);
	}
	
	public Array<TiledMapTile> getSeekerSpawns(){
		return seekerSpawns;
	}
	
	public Vector2 getSneakerSpawn(){
		return sneakerSpawn;
	}
	
	public void dispose(){
		tileMap.dispose();
		mapRenderer.dispose();
	}
	
}
