package com.gearworks.state;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.gearworks.Game;
/*
public class MainMenuState implements State{
	//GUI
	
	Skin skin = new Skin();
	Stage stage = new Stage();
	
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
	
	// Texture for background 
	Texture backgroundTexture= new Texture("assets/splashscreen.png");
	Image background = new Image(backgroundTexture);
	stage.addActor(background);
	
	// Create a table that fills the screen. Everything else will go inside this table.
	Table table = new Table();
	table.setFillParent(true);
	stage.addActor(table);
	
	// Create a play button
	final TextButton playButton = new TextButton("Play", skin);
	playButton.setPosition(310,270);
	playButton.setSize(150,70);
	playButton.setColor(0,0,0,0);
	stage.addActor(playButton);
	
	// Add a listener to the button. ChangeListener is fired when the button's checked state changes, eg when clicked,
	// Button#setChecked() is called, via a key press, etc. If the event.cancel() is called, the checked state will be reverted.
	// ClickListener could have been used, but would only fire when clicked. Also, canceling a ClickListener event won't
	// revert the checked state.
	playButton.addListener(new ChangeListener() {
		@Override
		public void changed(ChangeEvent event, Actor actor) {
		System.out.println("Clicked! Is checked: " + playButton.isChecked());
		playButton.setText("Good job!");
		sm.setState(new ConnectState());
		stage.dispose();
		}
		@Override
		public void render(Game game) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void update(Game game) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onEnter(Game game) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void onExit(Game game) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public boolean canEnterState(Game game) {
			// TODO Auto-generated method stub
			return false;
		}
		@Override
		public boolean canExitState(Game game) {
			// TODO Auto-generated method stub
			return false;
		}
		@Override
		public int getId() {
			// TODO Auto-generated method stub
			return 0;
		}
	});
	
	// Add an image actor. Have to set the size, else it would be the size of the drawable (which is the 1x1 texture).
	//table.add(new Image(skin.newDrawable("white", Color.RED))).size(64);
	
	inputMultiplexer.addProcessor(stage);
	
	inputMultiplexer.addProcessor(ui);
	
	//sm.setState(new ConnectState()); //Set state after we are done initializing everything
	}
} */