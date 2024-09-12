package org.dungeon.stats;

import java.io.Serializable;

import org.dungeon.achievements.BattleStatisticsRequirement;
import org.dungeon.entity.creatures.Creature;
import org.dungeon.game.PartOfDay;
import org.dungeon.util.CounterMap;

public class BattleStatistics implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final CounterMap<BattleRecord> records = new CounterMap<BattleRecord>();
	
	public void addBattle(Creature foe, CauseOfDeath causeOfDeath, PartOfDay partOfDay) {
		BattleRecord record = new BattleRecord(foe.getID(), foe.getType(), causeOfDeath, partOfDay);
		records.incrementCounter(record);
	}
	
	public CounterMap<CauseOfDeath> getKillsByCauseOfDeath() {
		CounterMap<CauseOfDeath> causeOfDeathCounterMap = new CounterMap<CauseOfDeath>();
		for (BattleRecord record : records.keySet()) {
			causeOfDeathCounterMap.incrementCounter(record.getCauseOfDeath());
		}
		return causeOfDeathCounterMap;
	}
	
	public boolean satisfies(BattleStatisticsRequirement requirement) {
		int count = 0;
		for (BattleRecord record : records.keySet()) {
			if (requirement.getQuery().matches(record)) {
				count += records.getCounter(record);
				if (count >= requirement.getCount()) {
					return true;
				}
			}
		}
		return false;
	}

}
