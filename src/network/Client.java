package network;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;

import views.OnlineChampionPanel;
import model.abilities.*;
import model.world.Champion;
import engine.Game;
import engine.Player;

public class Client{
	
	private int playerNumber = 0;
	private Player player;
	private String name, ip;
	private Game game;
	private ArrayList<Champion> champions;
	private ArrayList<Ability> abilities;
	private SocketAction s;
	private boolean gameStarted;
	
	public Client(String name, String ip){
		this.name = name;
		this.ip = ip;
	}
	public void connect(){
		
		try {
			Socket socket = new Socket(ip, 7378);

			s = new SocketAction(socket);
			s.send(name);
			
			String in = "";
			try {
				in = s.receive();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (in.equals("1")){
				playerNumber = 1;
			}
			else if (in.equals("2")){
				playerNumber = 2;
			}
			System.out.println(name + " connected as player " + playerNumber);
			waitForGame();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void waitForGame(){
		GameMessage gm = (GameMessage) s.receiveObject();
		
		Game.availableAbilities = (ArrayList<Ability>) s.receiveObject();
		System.out.println("Received Abilities");
		
		Game.availableChampions = (ArrayList<Champion>) s.receiveObject();
		System.out.println("Received Champions");
		
		game = (Game)s.receiveObject();
		System.out.println("Received Game");
		
		if (playerNumber == 1)
			player = game.getFirstPlayer();
		else
			player = game.getSecondPlayer();
		
		gameStarted = true;
		
	}
	public void send(Object o){
		s.sendObject(o);
	}
	public Object receive(){
		return s.receiveObject();
	}
	public void sendChampion(Champion champion) {
		s.sendObject(game);
		s.sendObject(champion);
	}
	public Champion receiveChampion(){
		game = (Game)s.receiveObject();
		if (playerNumber == 1)
			player = game.getFirstPlayer();
		else
			player = game.getSecondPlayer();
		return (Champion) s.receiveObject();
	}
	
	public Game getGame(){
		return game;
	}
	public void setGame(Game game){
		this.game = game;
	}
	public Player getPlayer(){
			return player;
	}
	public Player getOtherPlayer(){
		if (player.equals(game.getFirstPlayer()))
			return game.getSecondPlayer();
		return game.getFirstPlayer();
	}
	public int getPlayerNumber(){
		return playerNumber;
	}
	public boolean isGameStarted(){
		return gameStarted;
	}
	
}
