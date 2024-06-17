package main.java.org.dungeon.game;

public class GameState {
	
	//TODO: finish GameState class
	
	private final World world;
	
	private Point heroPosition;
	
	public GameState() {
		world = new World();
		
		heroPosition = new Point(0, 0);
	}
	
	public World getWorld() {
		return world;
	}

	public Point getHeroPosition() {
		return heroPosition;
	}

}
