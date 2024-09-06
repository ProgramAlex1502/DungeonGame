package org.dungeon.stats;

import java.io.Serializable;

import org.dungeon.entity.creatures.Creature;
import org.dungeon.game.ID;
import org.dungeon.util.CounterMap;

public class BattleStatistics implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final CounterMap<String> killsByCreatureType;
	private final CounterMap<ID> killsByCreatureID;
	private final CounterMap<CauseOfDeath> killsByCauseOfDeath;
	private int battleCount;
	private int longestBattleLength;
	
	public BattleStatistics() {
		killsByCreatureType = new CounterMap<String>();
		killsByCreatureID = new CounterMap<ID>();
		killsByCauseOfDeath = new CounterMap<CauseOfDeath>();
	}
	
	public void addBattle(Creature foe, CauseOfDeath causeOfDeath, int turns) {
		battleCount++;
		killsByCreatureType.incrementCounter(foe.getType());
		killsByCreatureID.incrementCounter(foe.getID());
		killsByCauseOfDeath.incrementCounter(causeOfDeath);
		longestBattleLength = Math.max(longestBattleLength, turns);
	}
	
	public int getBattleCount() {
		return battleCount;
	}
	
	public int getLongestBattleLength() {
		return longestBattleLength;
	}
	
	public CounterMap<String> getKillsByCreatureType() {
		return killsByCreatureType;
	}

	public CounterMap<ID> getKillsByCreatureID() {
		return killsByCreatureID;
	}
	
	public CounterMap<CauseOfDeath> getKillsByCauseOfDeath() {
		return killsByCauseOfDeath;
	}

}
