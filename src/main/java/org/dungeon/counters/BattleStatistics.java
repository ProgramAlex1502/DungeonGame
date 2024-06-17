package main.java.org.dungeon.counters;

import java.io.Serializable;

import main.java.org.dungeon.creatures.Creature;
import main.java.org.dungeon.items.Item;
import main.java.org.dungeon.utils.Constants;

public class BattleStatistics implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private final CounterMap<String> killsByCreatureId;
	private final CounterMap<String> killsByCreatureType;
	private final CounterMap<String> killsByWeapon;
	private int battleCount;
	private int longestBattleLength;
	
	public BattleStatistics() {
		killsByCreatureId = new CounterMap<String>();
		killsByCreatureType = new CounterMap<String>();
		killsByWeapon = new CounterMap<String>();
	}
	
	public void addBattle(Creature attacker, Creature defender, boolean attackerWon, int turns) {
		if (attackerWon) {
			killsByCreatureId.incrementCounter(defender.getId());
			killsByCreatureType.incrementCounter(defender.getType());
			Item weapon = attacker.getWeapon();
			killsByWeapon.incrementCounter(weapon != null ? weapon.getId() : Constants.UNARMED_ID);
		}
		battleCount++;
		if (turns > longestBattleLength) {
			longestBattleLength = turns;
		}
	}
	
	public int getBattleCount() {
		return battleCount;
	}
	
	public int getLongestBattleLength() {
		return longestBattleLength;
	}
	
	public CounterMap<String> getKillsByCreatureId() {
		return killsByCreatureId;
	}
	
	public CounterMap<String> getKillsByCreatureType() {
		return killsByCreatureType;
	}
	
	public CounterMap<String> getKillsByWeapon() {
		return killsByWeapon;
	}

}
