package com.gearworks;

import java.io.IOException;
import java.util.ArrayList;

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
import com.badlogic.gdx.math.Vector3;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.gearworks.shared.*;
import com.gearworks.state.GameState;
import com.gearworks.state.State;
import com.gearworks.state.StateManager;

public class Game implements ApplicationListener {
	public static final String 	TITLE = "Seek and Sneak";
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
	private Client client;
	private Level level;

	private SpriteBatch batch;
	private ShapeRenderer renderer;
	private Player player;
	
	@Override
	public void create() {	
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
		sm.setState(new GameState());
		
		font = new BitmapFont();
		font.setScale(.8f);
		font.setColor(Color.WHITE);
		
		batch = new SpriteBatch();	
		renderer = new ShapeRenderer();
		
		
		//Client setup
		client = new Client();
		
		Kryo kryo = client.getKryo();
		kryo.register(Message.class);
		
		client.start();
		/*
		try {
			client.connect(5000, "10.34.23.26", 60420, 60421);
		} catch (IOException e) {
			e.printStackTrace();
		}
		client.addListener(new ClientListener());
		*/
		
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
			
			sm.update();
			camera.update();
			followPlayer(); //Tell camera to follow Player
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
	
	/* Makes the camera follow the currently selected player */
	public void followPlayer(){
		if(ui == null){
			camera.position.set(new Vector3());
		}else if(ui.activeCharacter() != null){
			camera.position.set(camera.position.x + (ui.activeCharacter().position().x - camera.position.x )/8, 
								camera.position.y + (ui.activeCharacter().position().y  -camera.position.y)/8, 
								camera.position.z);
		}
	}
	
	public void destroy(Entity ent){
		if(state() == null) return;
		
		ent.dispose();
		entities.remove(ent);
	}
	
	public ArrayList<Entity> entities() {
		return entities;
	}
	
	public Level level(){
		return level;
	}
	
	public void level(Level level){
		this.level = level;
	}
	
	public OrthographicCamera camera(){ return camera; }
	public State state(){ return sm.state(); }
	public SpriteBatch batch() { return batch; }	
	public ShapeRenderer renderer() { return renderer; }
	public UserInterface ui(){ return ui; }
	public Player player(){ return player; }
	public void player(Player pl){ player = pl; }
}
