package org.dungeon.achievements;

import org.dungeon.game.Game;
import org.dungeon.game.ID;
import org.dungeon.stats.ExplorationStatistics;
import org.dungeon.util.CounterMap;

final class ExplorationComponent {
	
	final CounterMap<ID> killsByLocationID;
	final CounterMap<ID> visitedLocations;
	final CounterMap<ID> maximumNumberOfVisits;
	
	ExplorationComponent(CounterMap<ID> killsByLocationID, CounterMap<ID> visitedLocations,
			CounterMap<ID> maximumNumberOfVisits) {
		this.killsByLocationID = killsByLocationID;
		this.visitedLocations = visitedLocations;
		this.maximumNumberOfVisits = maximumNumberOfVisits;
	}

	public boolean isFulfilled() {
		ExplorationStatistics statistics = Game.getGameState().getStatistics().getExplorationStatistics();
		
		if (killsByLocationID != null) {
			for (ID locationID : killsByLocationID.keySet()) {
				if (statistics.getKillCount(locationID) < killsByLocationID.getCounter(locationID)) {
					return false;
				}
			}			
		}
		if (visitedLocations != null) {
			for (ID locationID : visitedLocations.keySet()) {
				if (statistics.getVisitedLocations(locationID) < visitedLocations.getCounter(locationID)) {
					return false;
				}
			}			
		}
		if (maximumNumberOfVisits != null) {
			for (ID locationID : maximumNumberOfVisits.keySet()) {
				if (statistics.getVisitedLocations(locationID) < maximumNumberOfVisits.getCounter(locationID)) {
					return false;
				}
			}			
		}
		
		return true;
	}

}
