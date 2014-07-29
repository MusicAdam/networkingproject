package com.gearworks.shared;

public class EndGame extends Message {
	
	public String message;
	
	public EndGame(){
		message = "this is a no arguement constructor";
	}
	
	public EndGame(String msg){
		
		message = msg;
		
	}

}
