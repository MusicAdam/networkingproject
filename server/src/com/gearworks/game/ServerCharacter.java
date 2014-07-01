package com.gearworks.game;
import com.badlogic.gdx.math.Vector2;
import com.gearworks.Game;
import com.gearworks.shared.Character;
import com.gearworks.shared.Player;

public class ServerCharacter extends Character {
	
	private Instance instance;

	public ServerCharacter(Player player, Game cRef, Instance i) {
		super(player, cRef);
		instance = i;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public void tile(int x, int y){
		this.x = x;
		this.y = y;
		
		Vector2 position = instance.level().positionFromIndex(x, y);
		position(position);
	}

}
