package network;

public class GameServer {
	public static void main(String args[ ]) {
		System.out.println("Game server up and running...");
		new GameDaemon().start();
	}
}
