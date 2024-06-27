package main.java.org.dungeon.achievements;

import main.java.org.dungeon.game.Game;
import main.java.org.dungeon.game.ID;
import main.java.org.dungeon.stats.BattleStatistics;
import main.java.org.dungeon.util.CounterMap;

public class BattleComponent {
	
	final int minimumBattleCount;
	final int longestBattleLength;
	final CounterMap<ID> killsByCreatureID;
	final CounterMap<String> killsByCreatureType;
	final CounterMap<ID> killsByWeapon;
	
	BattleComponent(int minimumBattleCount, int longestBattleLength, CounterMap<ID> killsByCreatureID,
			CounterMap<String> killsByCreatureType, CounterMap<ID> killsByWeapon) {
		this.minimumBattleCount = minimumBattleCount;
		this.longestBattleLength = longestBattleLength;
		this.killsByCreatureID = killsByCreatureID;
		this.killsByCreatureType = killsByCreatureType;
		this.killsByWeapon = killsByWeapon;
	}

	public boolean isFulfilled() {
		BattleStatistics statistics = Game.getGameState().getStatistics().getBattleStatistics();
		return statistics.getBattleCount() >= minimumBattleCount
			&& statistics.getLongestBattleLength() >= longestBattleLength
			&& (killsByCreatureID == null || statistics.getKillsByCreatureID().fulfills(killsByCreatureID))
			&& (killsByCreatureType == null || statistics.getKillsByCreatureType().fulfills(killsByCreatureType))
			&& (killsByWeapon == null || statistics.getKillsByWeapon().fulfills(killsByWeapon));
	}

}
