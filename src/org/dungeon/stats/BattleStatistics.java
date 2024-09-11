package org.dungeon.stats;

import java.io.Serializable;

import org.dungeon.entity.creatures.Creature;
import org.dungeon.game.ID;
import org.dungeon.util.CounterMap;
import org.dungeon.util.Utils;

public class BattleStatistics implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final CounterMap<String> killsByCreatureType = new CounterMap<String>();
	private final CounterMap<ID> killsByCreatureID = new CounterMap<ID>();
	private final CounterMap<CauseOfDeath> killsByCauseOfDeath = new CounterMap<CauseOfDeath>();
	private final Record longestBattleLength = new Record(Record.Type.MAXIMUM);
	private int battleCount;
	
	public void addBattle(Creature foe, CauseOfDeath causeOfDeath, int turns) {
		battleCount++;
		killsByCreatureType.incrementCounter(foe.getType());
		killsByCreatureID.incrementCounter(foe.getID());
		killsByCauseOfDeath.incrementCounter(causeOfDeath);
		longestBattleLength.update(turns);
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
	
	public int getLongestBattleLength() {
		return Utils.zeroIfNull(longestBattleLength.getValue());
	}
	
	public int getBattleCount() {
		return battleCount;
	}

}
