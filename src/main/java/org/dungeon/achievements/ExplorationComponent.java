package main.java.org.dungeon.achievements;

import main.java.org.dungeon.counters.ExplorationLog;
import main.java.org.dungeon.creatures.Hero;
import main.java.org.dungeon.game.ID;
import main.java.org.dungeon.util.CounterMap;

public class ExplorationComponent {

	final CounterMap<ID> killCounter = new CounterMap<ID>();
	
	final CounterMap<ID> distinctLocationsVisitCount = new CounterMap<ID>();
	
	final CounterMap<ID> sameLocationVisitCounter = new CounterMap<ID>();
	
	boolean isFulfilled(Hero hero) {
		ExplorationLog explorationLog = hero.getExplorationLog();
		for (ID locationID : killCounter.keySet()) {
			if (explorationLog.getKillCount(locationID) < killCounter.getCounter(locationID)) {
				return false;
			}
		}
		for (ID locationID : distinctLocationsVisitCount.keySet()) {
			if (explorationLog.getDistinctVisitCount(locationID) < distinctLocationsVisitCount.getCounter(locationID)) {
				return false;
			}
		}
		for (ID locationID : sameLocationVisitCounter.keySet()) {
			if (explorationLog.getSameLocationVisitCount(locationID) < sameLocationVisitCounter.getCounter(locationID)) {
				return false;
			}
		}
		
		return true;
	}

}
