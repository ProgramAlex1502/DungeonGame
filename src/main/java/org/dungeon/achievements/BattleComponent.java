package main.java.org.dungeon.achievements;

import main.java.org.dungeon.counters.BattleStatistics;
import main.java.org.dungeon.counters.CounterMap;
import main.java.org.dungeon.creatures.Hero;

public class BattleComponent extends AchievementComponent{
	
	int battleCount;
	int longestBattleLength;
	CounterMap<String> killsByWeapon;
	CounterMap<String> killsByCreatureId;
	CounterMap<String> killsByCreatureType;
	
	public BattleComponent() {
		killsByWeapon = new CounterMap<String>();
		killsByCreatureId = new CounterMap<String>();
		killsByCreatureType = new CounterMap<String>();
	}

	@Override
	public boolean isFulfilled(Hero hero) {
		BattleStatistics stats = hero.getBattleStatistics();
		if (stats.getBattleCount() < battleCount) {
			return false;
		}
		if (stats.getLongestBattleLength() < longestBattleLength) {
			return false;
		}
		if (killsByCreatureId != null && !stats.getKillsByCreatureId().fulfills(killsByCreatureId)) {
			return false;
		}
		if (killsByCreatureType != null && !stats.getKillsByCreatureType().fulfills(killsByCreatureType)) {
			return false;
		}
		if (killsByWeapon != null && !stats.getKillsByWeapon().fulfills(killsByWeapon)) {
			return false;
		}
		return true;
	}

}
