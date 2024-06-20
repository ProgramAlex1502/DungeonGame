package main.java.org.dungeon.game;

import main.java.org.dungeon.creatures.Hero;
import main.java.org.dungeon.utils.CommandHistory;

public class GameState {
	
	//TODO: finish GameState class
	
	private final CommandHistory commandHistory;
	private final World world;
	
	private final Hero hero;
	private Point heroPosition;
	
	private boolean bold;
	
	private boolean usingBars;
	
	transient private boolean saved;
	
	public GameState() {
		commandHistory = new CommandHistory();
		world = new World();
		
		hero = new Hero("Seth");
		heroPosition = new Point(0, 0);
		
		saved = true;
	}
	
	public CommandHistory getCommandHistory() {
		return commandHistory;
	}
	
	public World getWorld() {
		return world;
	}
	
	public Hero getHero() {
		return hero;
	}

	public Point getHeroPosition() {
		return heroPosition;
	}
	
	public void setHeroPosition(Point heroPosition) {
		this.heroPosition = heroPosition;
	}
	
	public boolean isBold() {
		return bold;
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
	
	public Location getHeroLocation() {
		return world.getLocation(heroPosition);
	}

}
