package org.dungeon.achievements;

import java.util.Collection;

import org.dungeon.game.Game;
import org.dungeon.stats.BattleStatistics;

public class BattleComponent {
	
	private final Collection<BattleStatisticsRequirement> requirements;
	
	BattleComponent(Collection<BattleStatisticsRequirement> requirements) {
		this.requirements = requirements;
	}
	
	public boolean isFulfilled() {
		BattleStatistics battleStatistics = Game.getGameState().getStatistics().getBattleStatistics();
		for (BattleStatisticsRequirement requirement : requirements) {
			if (!battleStatistics.satisfies(requirement)) {
				return false;
			}
		}
		return true;
	}

}
