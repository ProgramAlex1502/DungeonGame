package main.java.org.dungeon.achievements;

import main.java.org.dungeon.counters.BattleStatistics;
import main.java.org.dungeon.counters.CounterMap;
import main.java.org.dungeon.creatures.Hero;
import main.java.org.dungeon.game.ID;

public class BattleComponent extends AchievementComponent{
	
	int battleCount;
	int longestBattleLength;
	final CounterMap<String> killsByCreatureType = new CounterMap<String>();
	final CounterMap<ID> killsByCreatureId = new CounterMap<ID>();
	final CounterMap<ID> killsByWeapon = new CounterMap<ID>();
	
	public BattleComponent() {
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
		if (!stats.getKillsByCreatureId().fulfills(killsByCreatureId)) {
			return false;
		}
		if (!stats.getKillsByCreatureType().fulfills(killsByCreatureType)) {
			return false;
		}
		if (!stats.getKillsByWeapon().fulfills(killsByWeapon)) {
			return false;
		}
		return true;
	}

}
