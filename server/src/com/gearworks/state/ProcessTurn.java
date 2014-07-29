package com.gearworks.state;

import com.badlogic.gdx.math.Vector2;
import com.gearworks.Game;
import com.gearworks.game.Instance;
import com.gearworks.game.ServerCharacter;
import com.gearworks.game.ServerPlayer;
import com.gearworks.shared.Character;
import com.gearworks.shared.EndGame;
import com.gearworks.shared.Player.Team;

public class ProcessTurn implements State{
	public Instance instance;
	private Vector2[] moves;
	
	public ProcessTurn(Instance instance, Vector2[] moves){
		this.instance = instance;
		this.moves = moves;
	}

	@Override
	public void render(Game game) {
		// Auto-generated method stub
		
	}

	@Override
	public void update(Game game) {
		// Auto-generated method stub
		
	}

	@Override
	public void onEnter(Game game) {
		ServerPlayer pl = instance.activePlayer();
		
		int i = 0;
		for(ServerCharacter c : pl.serverCharacters()){
			if(i < moves.length && moves[i] != null)
				c.tile((int)moves[i].x, (int)moves[i].y);
			i++;
		}
		
		if(instance.activePlayer().team() == Team.Seeker) //Only increment turn count when both players have moved, this will indicate a full turn
			instance.turncount(instance.turncount() + 1);
		
		ServerPlayer winner = game.checkVictory(instance);
		
		if(winner != null){
			//that player wins.
			winner.connection().sendTCP(new EndGame("You win!!"));
			if(instance.players()[0] == winner){
				instance.players()[1].connection().sendTCP(new EndGame("You Lose!!"));
			}else{
				instance.players()[0].connection().sendTCP(new EndGame("You Lose!!"));				
			}
			return;
		}
		
		//Toggle active player
		if(instance.activePlayer().equals(instance.players()[0])){
			instance.activePlayer(instance.players()[1]);
		}else{
			instance.activePlayer(instance.players()[0]);
		}
		
		instance.sm().setState(new PlayerTurn(instance)); //sends new PlayerTurn message
	}

	@Override
	public void onExit(Game game) {
		
	}

	@Override
	public boolean canEnterState(Game game) {
		return true;
	}

	@Override
	public boolean canExitState(Game game) {
		return true;
	}

	@Override
	public int getId() {
		return 3;
	}

}
