package com.gearworks;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.gearworks.messages.TestMessage;

public class ServerListener extends Listener{
	@Override
	 public void received (Connection connection, Object object) {
		if(object instanceof TestMessage){
			TestMessage msg = (TestMessage)object;
			System.out.println(msg.test);
		}
	}
}
