package com.gearworks;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.gearworks.shared.StartTurn;

public class ClientListener extends Listener{
	
	public void received (Connection connection, Object object){
		if(object instanceof StartTurn){
			StartTurn msg = (StartTurn)object;
			
			//level.updateHiddenCells(msg.hiddenCells); //Updates the lighting data
			//level.updateEnemies(msg.visibleEnemies); //Updates which enemies are visible
			
			//if(msg.active.id() == game.player().id()){
			//	game.setActive(); //Tell the game its now our turn
			//}
		}
	}

}
