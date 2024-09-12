package org.dungeon.achievements;

public class BattleStatisticsRequirement {
	
	private final BattleStatisticsQuery query;
	private final int count;
	
	public BattleStatisticsRequirement(BattleStatisticsQuery query, int count) {
		if (count < 1) {
			throw new IllegalArgumentException("count must be positive.");
		}
		this.query = query;
		this.count = count;
	}
	
	public BattleStatisticsQuery getQuery() {
		return query;
	}
	
	public int getCount() {
		return count;
	}
	
	@Override
	public String toString() {
		return String.format("BattleStatisticsRequirement{query=%s, count%d}", query, count);
	}

}
