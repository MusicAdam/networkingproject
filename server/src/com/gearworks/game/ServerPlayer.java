package com.gearworks.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
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
			ServerCharacter sneeker = new ServerCharacter(this, inst.game, inst);
			sneeker.tile((int)inst.level().getSneakerSpawn().x, (int)inst.level().getSneakerSpawn().y);
			characters.add(sneeker);
		}else{
			for(Vector2 index : inst.level().getSeekerSpawns()){
				ServerCharacter seeker = new ServerCharacter(this, inst.game, inst);
				seeker.tile((int)index.x, (int)index.y);
				characters.add(seeker);
			}
		}
	}
	
	public Array<ServerCharacter> serverCharacters(){
		Array<ServerCharacter> serverCharacters = new Array<ServerCharacter>();
		for(Character c : characters){
			if(c instanceof ServerCharacter){
				serverCharacters.add((ServerCharacter)c);
			}
		}
		
		return serverCharacters;
	}

}
