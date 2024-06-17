package main.java.org.dungeon.items;

import java.io.Serializable;

import org.joda.time.DateTime;

import main.java.org.dungeon.game.Engine;
import main.java.org.dungeon.game.Game;
import main.java.org.dungeon.utils.Constants;

public class ClockComponent implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Item master;
	
	private DateTime lastTime;
	
	public void setMaster(Item master) {
		this.master = master;
	}
	
	public void setLastTime(DateTime lastTime) {
		this.lastTime = lastTime;
	}
	
	public String getTimeString() {
		if (master.isBroken()) {
			if (lastTime == null) {
				if (Engine.RANDOM.nextBoolean()) {
					return "The clock is pure junk.";
				} else {
					return "The clock is completely smashed.";
				}
			} else {
				return "The clock is broken. Still, it displays " + Constants.TIME_FORMAT.print(lastTime) + ".";
			}
		} else {
			String timeString = Constants.TIME_FORMAT.print(Game.getGameState().getWorld().getWorldDate());
			return "The clock displays " + timeString + ".";
		}
	}

}
