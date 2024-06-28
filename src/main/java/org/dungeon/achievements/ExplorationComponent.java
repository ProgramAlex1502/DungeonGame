package main.java.org.dungeon.achievements;

import main.java.org.dungeon.game.Game;
import main.java.org.dungeon.game.ID;
import main.java.org.dungeon.stats.ExplorationStatistics;
import main.java.org.dungeon.util.CounterMap;

public class ExplorationComponent {

	final CounterMap<ID> killsByLocationID;
	
	final CounterMap<ID> discoveredLocations;
	
	final CounterMap<ID> maximumNumberOfVisits;
	
	ExplorationComponent(CounterMap<ID> killsByLocationID, CounterMap<ID> discoveredLocations,
			CounterMap<ID> maximumNumberOfVisits) {
		this.killsByLocationID = killsByLocationID;
		this.discoveredLocations = discoveredLocations;
		this.maximumNumberOfVisits = maximumNumberOfVisits;
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
		if (discoveredLocations != null) {
			for (ID locationID : discoveredLocations.keySet()) {
				if (explorationStatistics.getDiscoveredLocations(locationID) < discoveredLocations.getCounter(locationID)) {
					return false;
				}
			}			
		}
		if (maximumNumberOfVisits != null) {
			for (ID locationID : maximumNumberOfVisits.keySet()) {
				if (explorationStatistics.getMaximumNumberOfVisits(locationID) < maximumNumberOfVisits.getCounter(locationID)) {
					return false;
				}
			}			
		}
		
		return true;
	}

}
