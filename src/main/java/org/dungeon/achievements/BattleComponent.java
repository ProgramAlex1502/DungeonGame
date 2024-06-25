package main.java.org.dungeon.achievements;

import main.java.org.dungeon.counters.BattleStatistics;
import main.java.org.dungeon.creatures.Hero;
import main.java.org.dungeon.game.ID;
import main.java.org.dungeon.util.CounterMap;

public class BattleComponent {
	
	final CounterMap<String> killsByCreatureType = new CounterMap<String>();
	final CounterMap<ID> killsByCreatureID = new CounterMap<ID>();
	final CounterMap<ID> killsByWeapon = new CounterMap<ID>();

	int battleCount;
	int longestBattleLength;

	public boolean isFulfilled(Hero hero) {
		BattleStatistics stats = hero.getBattleStatistics();
		if (stats.getBattleCount() < battleCount) {
			return false;
		}
		if (stats.getLongestBattleLength() < longestBattleLength) {
			return false;
		}
		if (!stats.getKillsByCreatureID().fulfills(killsByCreatureID)) {
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
