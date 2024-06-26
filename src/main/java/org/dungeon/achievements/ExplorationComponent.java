package main.java.org.dungeon.achievements;

import main.java.org.dungeon.game.Game;
import main.java.org.dungeon.game.ID;
import main.java.org.dungeon.stats.ExplorationStatistics;
import main.java.org.dungeon.util.CounterMap;

public class ExplorationComponent {

	final CounterMap<ID> killCounter = new CounterMap<ID>();
	
	final CounterMap<ID> distinctLocationsVisitCount = new CounterMap<ID>();
	
	final CounterMap<ID> sameLocationVisitCounter = new CounterMap<ID>();
	
	boolean isFulfilled() {
		ExplorationStatistics explorationStatistics = Game.getGameState().getStatistics().getExplorationStatistics();
		for (ID locationID : killCounter.keySet()) {
			if (explorationStatistics.getKillCount(locationID) < killCounter.getCounter(locationID)) {
				return false;
			}
		}
		for (ID locationID : distinctLocationsVisitCount.keySet()) {
			if (explorationStatistics.getDistinctVisitCount(locationID) < distinctLocationsVisitCount.getCounter(locationID)) {
				return false;
			}
		}
		for (ID locationID : sameLocationVisitCounter.keySet()) {
			if (explorationStatistics.getSameLocationVisitCount(locationID) < sameLocationVisitCounter.getCounter(locationID)) {
				return false;
			}
		}
		
		return true;
	}

}
