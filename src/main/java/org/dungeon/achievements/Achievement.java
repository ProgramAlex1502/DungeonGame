package main.java.org.dungeon.achievements;

import main.java.org.dungeon.creatures.Hero;
import main.java.org.dungeon.io.IO;
import main.java.org.dungeon.utils.Constants;
import main.java.org.dungeon.utils.Utils;

public class Achievement {

	final String id;
	private final String name;
	private final String info;
	
	private BattleComponent battle;
	private ExplorationComponent exploration;
	
	public Achievement(String id, String name, String info) {
		this.id = id;
		this.name = name;
		this.info = info;
		battle = new BattleComponent();
		exploration = new ExplorationComponent();
	}
	
	public String getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getInfo() {
		return info;
	}
	
	public void setBattleCount(int battleCount) {
		battle.battleCount = battleCount;
	}
	
	public void setLongestBattleLength(int longestBattleLength) {
		battle.longestBattleLength = longestBattleLength;
	}
	
	public void incrementKillsByWeapon(String id, int amount) {
		battle.killsByWeapon.incrementCounter(id, amount);
	}
	
	public void incrementKillByCreatureId(String id, int amount) {
		battle.killsByCreatureId.incrementCounter(id, amount);
	}
	
	public void incrementKillByCreatureType(String id, int amount) {
		battle.killsByCreatureType.incrementCounter(id, amount);
	}
	
	public void setKillCount(int count) {
		exploration.killCount = count;
	}
	
	public void setVisitCount(int count) {
		exploration.visitCount = count;
	}
	
	public boolean isFulfilled(Hero hero) {
		return battle.isFulfilled(hero) && exploration.isFulfilled(hero);
	}
	
	public final boolean update(Hero hero) {
		if (!hero.getAchievementTracker().isUnlocked(this) && isFulfilled(hero)) {
			printAchievementUnlocked();
			hero.getAchievementTracker().unlock(this);
			return true;
		} else {
			return false;
		}
	}
	
	void printAchievementUnlocked() {
		StringBuilder sb = new StringBuilder(225);
		sb.append(Utils.centerString(Constants.ACHIEVEMENT_UNLOCKED, '-')).append("\n");
		sb.append(getName()).append("\n");
		sb.append(getInfo()).append("\n");
		
		IO.writeString(sb.toString());
	}
}
