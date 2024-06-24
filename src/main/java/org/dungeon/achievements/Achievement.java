package main.java.org.dungeon.achievements;

import main.java.org.dungeon.creatures.Hero;
import main.java.org.dungeon.game.ID;
import main.java.org.dungeon.io.IO;
import main.java.org.dungeon.utils.Constants;
import main.java.org.dungeon.utils.Utils;

public class Achievement {

	private final ID id;
	private final String name;
	private final String info;
	
	private BattleComponent battle;
	private ExplorationComponent exploration;
	
	public Achievement(String id, String name, String info) {
		this.id = new ID(id);
		this.name = name;
		this.info = info;
		battle = new BattleComponent();
		exploration = new ExplorationComponent();
	}
	
	public ID getId() {
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
	
	public void incrementKillsByWeapon(ID id, int amount) {
		battle.killsByWeapon.incrementCounter(id, amount);
	}
	
	public void incrementKillsByCreatureId(ID id, int amount) {
		battle.killsByCreatureId.incrementCounter(id, amount);
	}
	
	public void incrementKillsByCreatureType(String id, int amount) {
		battle.killsByCreatureType.incrementCounter(id, amount);
	}
	
	public void setKillCount(int count) {
		exploration.killCount = count;
	}
	
	public void setVisitCount(int count) {
		exploration.visitCount = count;
	}
	
	boolean isFulfilled(Hero hero) {
		return battle.isFulfilled(hero) && exploration.isFulfilled(hero);
	}
	
	public final void update(Hero hero) {
		if (!hero.getAchievementTracker().isUnlocked(this) && isFulfilled(hero)) {
			printAchievementUnlocked();
			hero.getAchievementTracker().unlock(this);
			return;
		} else {
			return;
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
