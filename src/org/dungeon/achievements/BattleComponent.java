package org.dungeon.achievements;

import org.dungeon.game.Game;
import org.dungeon.game.ID;
import org.dungeon.stats.BattleStatistics;
import org.dungeon.stats.CauseOfDeath;
import org.dungeon.util.CounterMap;

public class BattleComponent {
	
	final int minimumBattleCount;
	final int longestBattleLength;
	final CounterMap<ID> killsByCreatureID;
	final CounterMap<String> killsByCreatureType;
	final CounterMap<CauseOfDeath> killsByCauseOfDeath;
	
	BattleComponent(int minimumBattleCount, int longestBattleLength, CounterMap<ID> killsByCreatureID,
			CounterMap<String> killsByCreatureType, CounterMap<CauseOfDeath> killsByCauseOfDeath) {
		this.minimumBattleCount = minimumBattleCount;
		this.longestBattleLength = longestBattleLength;
		this.killsByCreatureID = killsByCreatureID;
		this.killsByCreatureType = killsByCreatureType;
		this.killsByCauseOfDeath = killsByCauseOfDeath;
	}

	public boolean isFulfilled() {
		BattleStatistics statistics = Game.getGameState().getStatistics().getBattleStatistics();
		return statistics.getBattleCount() >= minimumBattleCount
				&& statistics.getLongestBattleLength() >= longestBattleLength
				&& (killsByCreatureID == null || statistics.getKillsByCreatureID().fulfills(killsByCreatureID))
				&& (killsByCreatureType == null || statistics.getKillsByCreatureType().fulfills(killsByCreatureType))
				&& (killsByCauseOfDeath == null || statistics.getKillsByCauseOfDeath().fulfills(killsByCauseOfDeath));
	}

}
