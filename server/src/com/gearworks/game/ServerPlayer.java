package com.gearworks.game;

import com.badlogic.gdx.math.Vector2;
import com.esotericsoftware.kryonet.Connection;
import com.gearworks.Game;
import com.gearworks.shared.Character;
import com.gearworks.shared.Player;
import com.gearworks.shared.Player.Team;

public class ServerPlayer extends Player {

	public ServerPlayer(Connection c) {
		super(c);
	}
	
	public void spawnCharacters(Instance inst){
		if(team == Team.Sneaker){
			Character sneeker = new Character(this, inst.game);
			sneeker.tile((int)inst.level().getSneakerSpawn().x, (int)inst.level().getSneakerSpawn().y);
			characters.add(sneeker);
		}else{
			for(Vector2 index : inst.level().getSeekerSpawns()){
				Character seeker = new Character(this, inst.game);
				seeker.tile((int)index.x, (int)index.y);
				characters.add(seeker);
			}
		}
	}

}
