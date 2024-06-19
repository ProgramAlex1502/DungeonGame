package main.java.org.dungeon.game;

public class GameState {
	
	//TODO: finish GameState class
	
	private final World world;
	
	private Point heroPosition;
	
	private boolean usingBars;
	
	transient private boolean saved;
	
	public GameState() {
		world = new World();
		
		heroPosition = new Point(0, 0);
		
		saved = true;
	}
	
	public World getWorld() {
		return world;
	}

	public Point getHeroPosition() {
		return heroPosition;
	}
	
	public boolean isUsingBars() {
		return usingBars;
	}
	
	public boolean isSaved() {
		return saved;
	}
	
	public void setSaved(boolean saved) {
		this.saved = saved;
	}

}
