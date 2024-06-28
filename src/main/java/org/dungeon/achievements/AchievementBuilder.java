package main.java.org.dungeon.achievements;

import main.java.org.dungeon.game.ID;
import main.java.org.dungeon.util.CounterMap;

public class AchievementBuilder {
	
	private String id;
	private String name;
	private String info;
	private String text;
	private int minimumBattleCount;
	private int longestBattleLength;
	private CounterMap<ID> killsByCreatureID;
	private CounterMap<String> killsByCreatureType;
	private CounterMap<ID> killsByWeapon;
	private CounterMap<ID> killsByLocationID;
	private CounterMap<ID> discoveredLocations;
	private CounterMap<ID> maximumNumberOfVisits;
	
	public AchievementBuilder setID(String id) {
		this.id = id;
		return this;
	}
	
	public AchievementBuilder setName(String name) {
		this.name = name;
		return this;
	}
	
	public AchievementBuilder setInfo(String info) {
		this.info = info;
		return this;
	}
	
	public AchievementBuilder setText(String text) {
		this.text = text;
		return this;
	}
	
	public AchievementBuilder setMinimumBattleCount(int minimumBattleCount) {
		this.minimumBattleCount = minimumBattleCount;
		return this;
	}
	
	public AchievementBuilder setLongestBattleLength(int longestBattleLength) {
		this.longestBattleLength = longestBattleLength;
		return this;
	}
	
	public AchievementBuilder setKillsByCreatureID(CounterMap<ID> killsByCreatureID) {
		if (killsByCreatureID.isNotEmpty()) {
			this.killsByCreatureID = killsByCreatureID;
		}
		return this;
	}
	
	public AchievementBuilder setKillsByCreatureType(CounterMap<String> killsByCreatureType) {
		if (killsByCreatureType.isNotEmpty()) {
			this.killsByCreatureType = killsByCreatureType;
		}
		return this;
	}
	
	public AchievementBuilder setKillsByWeapon(CounterMap<ID> killsByWeapon) {
		if (killsByWeapon.isNotEmpty()) {
			this.killsByWeapon = killsByWeapon;
		}
		return this;
	}
	
	public AchievementBuilder setKillsByLocationID(CounterMap<ID> killsByLocationID) {
		if (killsByLocationID.isNotEmpty()) {
			this.killsByLocationID = killsByLocationID;
		}
		return this;
	}
	
	public AchievementBuilder setDiscoveredLocations(CounterMap<ID> discoveredLocations) {
		if (discoveredLocations.isNotEmpty()) {
			this.discoveredLocations = discoveredLocations;
		}
		return this;
	}
	
	public AchievementBuilder setMaximumNumberOfVisits(CounterMap<ID> maximumNumberOfVisits) {
		if (maximumNumberOfVisits.isNotEmpty()) {
			this.maximumNumberOfVisits = maximumNumberOfVisits;
		}
		return this;
	}
	
	public Achievement createAchievement() {
		return new Achievement(id, name, info, text, minimumBattleCount, longestBattleLength, killsByCreatureID,
				killsByCreatureType, killsByWeapon, killsByLocationID, discoveredLocations, maximumNumberOfVisits);
	}

}
