package com.gearworks.game;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.gearworks.Game;
import com.gearworks.shared.Character;
import com.gearworks.shared.Entity;
import com.gearworks.shared.Player;
import com.gearworks.shared.Utils;
import com.gearworks.shared.Player.Team;

public class Level {
	public static final String MAP_LAYER = "map";
	public static final int	   TILE_SIZE = 32;
	public static final int TURNS_TO_WIN = 15;
	
	public Game game;
	
	private TiledMap tileMap;
	private OrthogonalTiledMapRenderer mapRenderer;
	protected Array<Vector2> seekerSpawns;
	protected Vector2 sneakerSpawn;
	protected Array<Vector2> hiddenCells;
	protected Vector2[] visibleEnemies; //Misnomer, this refers to all enemies, not just visible ones
	protected int mapWidth;
	protected int mapHeight;
	protected String file;
	
	public Level(Game game){
		this.game = game;
		visibleEnemies 	= null;
		this.hiddenCells = new Array<Vector2>();
		visibleEnemies = new Vector2[0];
		file = "";
	}
	
	public void load(String name){
		file = name;
		tileMap = new TmxMapLoader().load(name);
		mapRenderer = new OrthogonalTiledMapRenderer(tileMap);
		
		seekerSpawns = findSeekerSpawns();
		sneakerSpawn = findSneakerSpawn();
		

		TiledMapTileLayer layer;
		if((layer = (TiledMapTileLayer) tileMap.getLayers().get(MAP_LAYER)) != null){
			mapWidth = layer.getWidth();
			mapHeight = layer.getHeight();
		}
	}

	public void render(ShapeRenderer r) {
		if(mapRenderer == null) return;
				
		mapRenderer.setView(game.camera());
		mapRenderer.render();
		
		Gdx.gl.glEnable(GL20.GL_BLEND); //Need to enable blending for alpha rendering
		
		for(Vector2 cell : hiddenCells){
			Vector2 pos = positionFromIndex((int)cell.x, (int)cell.y);
			Utils.fillRect(r, new Color(0, 0, 0, .5f), pos.x, pos.y, TILE_SIZE, TILE_SIZE);
		}
		
		Gdx.gl.glDisable(GL20.GL_BLEND);
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
	
	public boolean isWall(int x, int y){
		TiledMapTileLayer.Cell cell = getCell(Level.MAP_LAYER, x, y);
		
		if(cell == null) return true;//Treat null as a wall that way we don't accidentally try and reference null
		
		return cell.getTile().getProperties().containsKey("isWall");
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
	
	protected Array<Vector2> findSeekerSpawns(){
		Array<Vector2> spawns = new Array<Vector2>();

		TiledMapTileLayer layer;
		if((layer = (TiledMapTileLayer) tileMap.getLayers().get(MAP_LAYER)) != null){
			for(int x = 0; x < layer.getWidth(); x++){
				for(int y = 0; y < layer.getHeight(); y++){
					TiledMapTileLayer.Cell cell = layer.getCell(x, y);
					
					if(cell != null){
						if(cell.getTile().getProperties().containsKey("seekerSpawn")){
							spawns.add(new Vector2(x,y));
						}
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
					if(cell != null){
						if(cell.getTile().getProperties().containsKey("sneakerSpawn")){
							return new Vector2(x, y);
						}
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
	
	//Calculates what a player can see, returns visible cells
		public Array<Vector2> calculateLighting(Player player){
			hiddenCells = new Array<Vector2>();
			
			Array<Vector2> visibleCells = new Array<Vector2>();
			for(Character c : player.characters()){
				Vector2 index = c.index();
				
				//Add the cell character is in
				visibleCells.add(c.index());
				
				//Handle seeker vision
				if(player.team() == Player.Team.Seeker){
					int pos = 1;
					
					//Calculate cells up
					while(!isWall((int)index.x, (int)index.y + pos)){
						visibleCells.add(new Vector2(index.x, index.y + pos));
						pos++;
					}
					
					pos = 1;
					
					//Calculate cells down
					while(!isWall((int)index.x, (int)index.y - pos)){
						visibleCells.add(new Vector2(index.x, index.y - pos));
						pos++;
					}
					
					pos = 1;
					
					//Calculate cells left
					while(!isWall((int)index.x - pos, (int)index.y)){
						visibleCells.add(new Vector2(index.x - pos, index.y));
						pos++;
					}
					
					pos = 1;
					
					//Calculate cells right
					while(!isWall((int)index.x + pos, (int)index.y)){
						visibleCells.add(new Vector2(index.x + pos, index.y));
						pos++;
					}
				}else{ //Handle sneaker vision
					//x^2 + y^2 = r
					//x = sqrt(r-y^2)
					//y = sqrt(r-x^2)
					
					float rsquared = (float) Math.pow(Character.SNEAKER_RADIUS, 2);
					
					//This isn't very efficient, have to iterate whole map twice (once here then once to invert)
					for(int x = 0; x < mapWidth; x++){
						for(int y = 0; y < mapHeight; y++){
							int cx1 = (int)index.x + (int)Math.floor(Math.sqrt(rsquared - Math.pow(y - index.y, 2)));
							int cx2 = (int)index.x - (int)Math.floor(Math.sqrt(rsquared - Math.pow(y - index.y, 2)));
							int cy1 = (int)index.y + (int)Math.floor(Math.sqrt(rsquared - Math.pow(x - index.x, 2)));
							int cy2 = (int)index.y - (int)Math.floor(Math.sqrt(rsquared - Math.pow(x - index.x, 2)));
							
							if(x < cx1 && y < cy1 && x > cx2 && y > cy2){
								visibleCells.add(new Vector2(x, y));
							}
						}
					}
				}
			}

			return visibleCells;
		}
	
	public Character characterInCell(Vector2 cell){
		for(Entity ent : game.entities()){
			if(ent instanceof Character){
				Character c = (Character)ent;
				
				if(c.index().equals(cell)){
					return c;
				}
			}else{
				System.out.println(ent.getClass().getCanonicalName() + " is not a character");
			}
		}
		
		return null;
	}
	
	//Recieves hidden cells from the server and stores it locally
	public void updateHiddenCells(Array<Vector2> hiddenCells){
		this.hiddenCells = hiddenCells;
	}

	public Array<Vector2> getSeekerSpawns(){
		return seekerSpawns;
	}
	
	public Vector2 getSneakerSpawn(){
		return sneakerSpawn;
	}
	
	public void dispose(){
		tileMap.dispose();
		mapRenderer.dispose();
	}
	
	public String file(){ return file; }
	
	
}
