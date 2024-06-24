package main.java.org.dungeon.achievements;

import main.java.org.dungeon.counters.BattleStatistics;
import main.java.org.dungeon.counters.CounterMap;
import main.java.org.dungeon.creatures.Hero;
import main.java.org.dungeon.game.ID;

public class BattleComponent extends AchievementComponent{
	
	int battleCount;
	int longestBattleLength;
	final CounterMap<String> killsByCreatureType;
	final CounterMap<ID> killsByCreatureId;
	final CounterMap<ID> killsByWeapon;
	
	public BattleComponent() {
		killsByCreatureType = new CounterMap<String>();
		killsByCreatureId = new CounterMap<ID>();
		killsByWeapon = new CounterMap<ID>();
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
