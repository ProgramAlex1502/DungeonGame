package main.java.org.dungeon.achievements;

import main.java.org.dungeon.creatures.Hero;
import main.java.org.dungeon.game.ID;
import main.java.org.dungeon.io.IO;
import main.java.org.dungeon.util.Constants;
import main.java.org.dungeon.util.Utils;

public class Achievement {

	private final ID id;
	private final String name;
	private final String info;
	
	private final BattleComponent battle = new BattleComponent();
	private final ExplorationComponent exploration = new ExplorationComponent();
	
	public Achievement(String id, String name, String info) {
		this.id = new ID(id);
		this.name = name;
		this.info = info;
	}
	
	public ID getID() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getInfo() {
		return info;
	}
	
	public void setMinimumBattleCount(int minimumBattleCount) {
		battle.battleCount = minimumBattleCount;
	}
	
	public void setLongestBattleLength(int longestBattleLength) {
		battle.longestBattleLength = longestBattleLength;
	}
	
	public void incrementKillsByWeapon(String id, int amount) {
		battle.killsByWeapon.incrementCounter(new ID(id), amount);
	}
	
	public void incrementKillsByCreatureID(String id, int amount) {
		battle.killsByCreatureID.incrementCounter(new ID(id), amount);
	}
	
	public void incrementKillsByCreatureType(String id, int amount) {
		battle.killsByCreatureType.incrementCounter(id, amount);
	}
	
	public void incrementKillsByLocationID(String locationID, int amount) {
		exploration.killCounter.incrementCounter(new ID(locationID), amount);
	}
	
	public void incrementVisitsToDistinctLocations(String locationID, int amount) {
		exploration.distinctLocationsVisitCount.incrementCounter(new ID(locationID), amount);
	}
	
	public void incrementVisitsToTheSameLocation(String locationID, int amount) {
		exploration.sameLocationVisitCounter.incrementCounter(new ID(locationID), amount);
	}
	
	boolean isFulfilled(Hero hero) {
		return battle.isFulfilled(hero) && exploration.isFulfilled(hero);
	}
	
	public final void update(Hero hero) {
		if (!hero.getAchievementTracker().isUnlocked(this) && isFulfilled(hero)) {
			printAchievementUnlocked();
			hero.getAchievementTracker().unlock(this);
		}
	}
	
	void printAchievementUnlocked() {
		IO.writeString(Utils.centerString(Constants.ACHIEVEMENT_UNLOCKED, '-') + "\n" + getName() + "\n" + getInfo());
	}
}
