package com.gearworks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Array.ArrayIterable;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.gearworks.game.Instance;
import com.gearworks.game.Level;
import com.gearworks.game.ServerPlayer;
import com.gearworks.shared.*;
import com.gearworks.shared.Character;
import com.gearworks.state.ReadyState;
import com.gearworks.state.State;
import com.gearworks.state.StateManager;

public class Game implements ApplicationListener {
	public static final String 	TITLE = "Seek and Sneak - Server";
	public static final int 	V_WIDTH = 800;
	public static final	int 	V_HEIGHT = 600;
	public static final float 	ASPECT_RATIO = (float)V_WIDTH/(float)V_HEIGHT;
	public static final int 	SCALE = 1;
	public static final float 	ZOOM = 5;
	
	public static final float STEP = 1 / 60f;
	private float accum;
	
	public BitmapFont font;
	
	protected StateManager sm;
	
	private Rectangle viewport;
	private boolean updateViewport;
	private OrthographicCamera camera;
	private FPSLogger fpsLogger;
	private InputMultiplexer inputMultiplexer;
	private UserInterface ui;
	private ArrayList<Entity> entities;
	private Level level;
	private Array<ServerPlayer> idlePlayers; 			//This is the list of players who are connected but not in game
	private Array<ServerPlayer> players; 				//All currently connected players
	private Array<Instance> instances;					//This is a list of all the active games
	private Queue<ServerPlayer> removePlayerQueue;		//Disconnected players waiting to be cleaned

	private SpriteBatch batch;
	private ShapeRenderer renderer;
	
	private Server server;
	
	@Override
	public void create() {	
		instances = new Array<Instance>();
		idlePlayers = new Array<ServerPlayer>();
		players = new Array<ServerPlayer>();
		removePlayerQueue = new ConcurrentLinkedQueue<ServerPlayer>();
		
		//Setup Server
		server = new Server();
		
		Kryo kryo = server.getKryo();
		kryo.register(ConnectMessage.class);
		kryo.register(ConnectedMessage.class);
		kryo.register(StartTurn.class);
		kryo.register(EndTurn.class);
		kryo.register(InvalidMove.class);
		kryo.register(GameFullMessage.class);
		kryo.register(Player.class);
		kryo.register(Array.class);
		kryo.register(Object[].class);
		kryo.register(Connection.class);
		kryo.register(Connection[].class);
		kryo.register(Server.class);
		kryo.register(Player.Team.class);
		kryo.register(Vector2[].class);
		kryo.register(ArrayIterable.class);
		kryo.register(Vector2.class);
		
		server.start();
		try {
			server.bind(60420, 60421);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		fpsLogger = new FPSLogger();
		
		entities = new ArrayList<Entity>();
		
		inputMultiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(inputMultiplexer);
		
		ui = new UserInterface(this);
		inputMultiplexer.addProcessor(ui);
		
		//Camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, V_WIDTH, V_HEIGHT);
		updateViewport = false;
		
		//State Manager
		sm = new StateManager(this);
		sm.setState(new ReadyState());
		
		font = new BitmapFont();
		font.setScale(.8f);
		font.setColor(Color.WHITE);
		
		batch = new SpriteBatch();	
		renderer = new ShapeRenderer();
	}

	@Override
	public void dispose() {
	}

	@Override
	public void render() {
		
		//Update viewport
		if(updateViewport){
			updateViewport = false;
			
	        Gdx.gl.glViewport((int) viewport.x, (int) viewport.y,
                    (int) viewport.width, (int) viewport.height);
	        
	        camera.viewportWidth = viewport.width;
	        camera.viewportHeight = viewport.height;
	        System.out.println("UPDATE VIEWPORT");
		}
		

		//Render
		Gdx.gl.glClearColor(.21f, .21f, .21f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		accum += Gdx.graphics.getDeltaTime();
		while(accum >= STEP) {
			accum -= STEP;
			
			//process queues
			if(!removePlayerQueue.isEmpty())
				removePlayer(removePlayerQueue.poll());
			
			sm.update();
			camera.update();
			
			//Update game instances (should be threaded to make scalable)
			for(Instance inst : instances){
				inst.update();
			}
		}
		

		sm.render();
		ui.render(batch, renderer);
		
		//fpsLogger.log();
	}

	@Override
	public void resize(int width, int height) {
		float aspectRatio = (float)width/(float)height;
		float scale = 1f;
		Vector2 crop = new Vector2(0f, 0f);
		
		if(aspectRatio > ASPECT_RATIO){
			scale = (float)height/(float)V_HEIGHT;
			crop.x = (width - V_WIDTH*scale)/2f;
		}else if(aspectRatio < ASPECT_RATIO){
			scale = (float)width/(float)V_WIDTH;
			crop.y = (height - V_HEIGHT*scale)/2f;
		}else{
			scale = (float)width/(float)V_WIDTH;
		}
		
		float w = (float)V_WIDTH*scale;
		float h = (float)V_HEIGHT*scale;
		
		viewport = new Rectangle(crop.x, crop.y, w, h);
		updateViewport = true;
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
	
	public Entity spawn(Entity ent){
		if(state() == null) return ent;

		entities.add(ent);
		
		return ent;
	}
	
	public void destroy(Entity ent){
		if(state() == null) return;
		
		ent.dispose();
		entities.remove(ent);
	}
	
	public Instance createInstance(ServerPlayer p1, ServerPlayer p2){
		Instance instance = new Instance(instances.size, p1, p2, this);
		instances.add(instance);		
		return instance;
	}
	
	public ArrayList<Entity> entities() {
		return entities;
	}
	
	public OrthographicCamera camera(){ return camera; }
	public State state(){ return sm.state(); }
	public SpriteBatch batch() { return batch; }	
	public ShapeRenderer renderer() { return renderer; }
	public Server server(){ return server; }
	public Array<ServerPlayer> idlePlayers(){ return idlePlayers; }

	public Level level() {
		return level;
	}

	public void level(Level level) {
		this.level = level;
	}
	
	public void addPlayer(ServerPlayer p){
		players.add(p);
		idlePlayers.add(p);
	}
	
	
	//Remove a player from the server, this means remove from idlePlayers, players, and end any instance currently running with the player
	private void removePlayer(ServerPlayer pl){
		players.removeValue(pl, false);
		idlePlayers.removeValue(pl, false);
		
		Instance inst = findInstanceByPlayer(pl);
		
		if(inst != null){
			inst.playerDisconnected(pl);
		}
		
		instances.removeValue(inst, true);
	}
	
	public Instance findInstanceByPlayer(Player pl){
		for(Instance i : instances){
			if(i.hasPlayer(pl)){
				return i;
			}
		}
		
		return null;
	}
	
	public Instance getInstance(int index){
		if(index < instances.size)
			return instances.get(index);
		return null;
	}
	
	public void setState(State s){
		sm.setState(s);
	}

	public ServerPlayer findPlayerByConnection(Connection connection) {
		for(ServerPlayer pl : players){
			if(pl.connection().equals(connection)){
				return pl;
			}
		}
		
		return null;
	}
	
	public void queueRemovePlayer(ServerPlayer pl){
		removePlayerQueue.add(pl);
	}
}
