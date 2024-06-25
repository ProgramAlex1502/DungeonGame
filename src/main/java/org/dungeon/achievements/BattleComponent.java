package main.java.org.dungeon.achievements;

import main.java.org.dungeon.game.Game;
import main.java.org.dungeon.game.ID;
import main.java.org.dungeon.stats.BattleStatistics;
import main.java.org.dungeon.util.CounterMap;

public class BattleComponent {
	
	final CounterMap<String> killsByCreatureType = new CounterMap<String>();
	final CounterMap<ID> killsByCreatureID = new CounterMap<ID>();
	final CounterMap<ID> killsByWeapon = new CounterMap<ID>();

	int battleCount;
	int longestBattleLength;

	public boolean isFulfilled() {
		BattleStatistics statistics = Game.getGameState().getStatistics().getBattleStatistics();
		if (statistics.getBattleCount() < battleCount) {
			return false;
		}
		if (statistics.getLongestBattleLength() < longestBattleLength) {
			return false;
		}
		if (!statistics.getKillsByCreatureID().fulfills(killsByCreatureID)) {
			return false;
		}
		if (!statistics.getKillsByCreatureType().fulfills(killsByCreatureType)) {
			return false;
		}
		if (!statistics.getKillsByWeapon().fulfills(killsByWeapon)) {
			return false;
		}
		return true;
	}

}
