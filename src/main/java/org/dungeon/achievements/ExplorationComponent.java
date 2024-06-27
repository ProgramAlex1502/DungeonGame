package main.java.org.dungeon.achievements;

import main.java.org.dungeon.game.Game;
import main.java.org.dungeon.game.ID;
import main.java.org.dungeon.stats.ExplorationStatistics;
import main.java.org.dungeon.util.CounterMap;

public class ExplorationComponent {

	final CounterMap<ID> killsByLocationID;
	
	final CounterMap<ID> distinctLocationsVisitCount;
	
	final CounterMap<ID> sameLocationVisitCounter;
	
	ExplorationComponent(CounterMap<ID> killsByLocationID, CounterMap<ID> distinctLocationsVisitCount,
			CounterMap<ID> sameLocationVisitCounter) {
		this.killsByLocationID = killsByLocationID;
		this.distinctLocationsVisitCount = distinctLocationsVisitCount;
		this.sameLocationVisitCounter = sameLocationVisitCounter;
	}
	
	boolean isFulfilled() {
		ExplorationStatistics explorationStatistics = Game.getGameState().getStatistics().getExplorationStatistics();
		if (killsByLocationID != null) {
			for (ID locationID : killsByLocationID.keySet()) {
				if (explorationStatistics.getKillCount(locationID) < killsByLocationID.getCounter(locationID)) {
					return false;
				}
			}			
		}
		if (distinctLocationsVisitCount != null) {
			for (ID locationID : distinctLocationsVisitCount.keySet()) {
				if (explorationStatistics.getDistinctVisitCount(locationID) < distinctLocationsVisitCount.getCounter(locationID)) {
					return false;
				}
			}			
		}
		if (sameLocationVisitCounter != null) {
			for (ID locationID : sameLocationVisitCounter.keySet()) {
				if (explorationStatistics.getSameLocationVisitCount(locationID) < sameLocationVisitCounter.getCounter(locationID)) {
					return false;
				}
			}			
		}
		
		return true;
	}

}
