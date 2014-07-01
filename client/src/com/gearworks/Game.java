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
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Array.ArrayIterable;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.gearworks.shared.*;
import com.gearworks.game.Level;
import com.gearworks.state.ConnectState;
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
	protected Queue<Vector2[]> visibleCellQueue;
	
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
	
	private String mapToLoad;
	
	Skin skin;
	Stage stage;
	
	@Override
	public void create() {	
		fpsLogger = new FPSLogger();
		
		entities = new ArrayList<Entity>();
		visibleCellQueue = new ConcurrentLinkedQueue<Vector2[]>();
		
		inputMultiplexer = new InputMultiplexer();
		Gdx.input.setInputProcessor(inputMultiplexer);
		
		ui = new UserInterface(this);
		
		//Camera
		camera = new OrthographicCamera();
		camera.setToOrtho(false, V_WIDTH, V_HEIGHT);
		updateViewport = false;
		
		//State Manager
		sm = new StateManager(this);
		
		font = new BitmapFont();
		font.setScale(.8f);
		font.setColor(Color.WHITE);
		
		batch = new SpriteBatch();	
		renderer = new ShapeRenderer();
		
		
		//Client setup
		client = new Client();
		client.start();
		
		Kryo kryo = client.getKryo();
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
		

		//GUI
		/*
		skin = new Skin();
		stage = new Stage();
		
		// Generate a 1x1 white texture and store it in the skin named "white".
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(Color.WHITE);
		pixmap.fill();
		skin.add("white", new Texture(pixmap));

		// Store the default libgdx font under the name "default".
		skin.add("default", new BitmapFont());

		// Configure a TextButtonStyle and name it "default". Skin resources are stored by type, so this doesn't overwrite the font.
		TextButtonStyle textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.down = skin.newDrawable("white", Color.DARK_GRAY);
		textButtonStyle.checked = skin.newDrawable("white", Color.BLUE);
		textButtonStyle.over = skin.newDrawable("white", Color.LIGHT_GRAY);
		textButtonStyle.font = skin.getFont("default");
		skin.add("default", textButtonStyle);

		// Create a table that fills the screen. Everything else will go inside this table.
		Table table = new Table();
		table.setFillParent(true);
		stage.addActor(table);

		// Create a button with the "default" TextButtonStyle. A 3rd parameter can be used to specify a name other than "default".
		final TextButton button = new TextButton("Click me!", skin);
		table.add(button);

		// Add a listener to the button. ChangeListener is fired when the button's checked state changes, eg when clicked,
		// Button#setChecked() is called, via a key press, etc. If the event.cancel() is called, the checked state will be reverted.
		// ClickListener could have been used, but would only fire when clicked. Also, canceling a ClickListener event won't
		// revert the checked state.
		button.addListener(new ChangeListener() {
			@Override
			public void changed (ChangeEvent event, Actor actor) {
				System.out.println("Clicked! Is checked: " + button.isChecked());
				button.setText("Good job!");
			}
		});

		// Add an image actor. Have to set the size, else it would be the size of the drawable (which is the 1x1 texture).
		table.add(new Image(skin.newDrawable("white", Color.RED))).size(64);

		inputMultiplexer.addProcessor(stage);
		*/
		inputMultiplexer.addProcessor(ui);
		
		sm.setState(new ConnectState()); //Set state after we are done initializing everything
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
			
			if(level() != null){
				if(visibleCellQueue.peek() != null)
					level().calculateHiddenCells(visibleCellQueue.poll());
			}
			
			sm.update();
			camera.update();
			followPlayer(); //Tell camera to follow Player
		}


		batch().setProjectionMatrix(camera().combined);
		sm.render();
		ui.render(batch, renderer);
		
		//stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
		//stage.draw();
		//Table.drawDebug(stage);
		
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
			camera.position.set(camera.position.x + (ui.activeCharacter().position().x + ui.activeCharacter().size().x/2 - camera.position.x )/8, 
								camera.position.y + (ui.activeCharacter().position().y + ui.activeCharacter().size().x/2 - camera.position.y)/8, 
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

	public Client client() {
		return client;
	}
	
	public boolean setState(State s){
		return sm.setState(s);
	}
	

	//Since we may receive the start turn message before the level has been loaded we need to be able to queue the messages to process when appropriate
	public void queueVisibleCells(Vector2[] visibleCells){
		visibleCellQueue.add(visibleCells);
	}
}
