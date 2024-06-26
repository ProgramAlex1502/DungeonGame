package main.java.org.dungeon.items;

import java.io.Serializable;

import main.java.org.dungeon.date.Date;
import main.java.org.dungeon.game.Engine;
import main.java.org.dungeon.game.Game;

public class ClockComponent implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Item master;
	
	private Date lastTime;
	
	public void setMaster(Item master) {
		this.master = master;
	}
	
	public void setLastTime(Date lastTime) {
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
				return "The clock is broken. Still, it displays " + lastTime.toTimeString() + ".";
			}
		} else {
			String timeString = Game.getGameState().getWorld().getWorldDate().toTimeString();
			return "The clock displays " + timeString + ".";
		}
	}

}
