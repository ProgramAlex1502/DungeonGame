package main.java.org.dungeon.counters;

import java.io.Serializable;

import main.java.org.dungeon.creatures.Creature;
import main.java.org.dungeon.game.ID;
import main.java.org.dungeon.items.Item;
import main.java.org.dungeon.util.Constants;
import main.java.org.dungeon.util.CounterMap;

public class BattleStatistics implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private final CounterMap<String> killsByCreatureType;
	private final CounterMap<ID> killsByCreatureID;
	private final CounterMap<ID> killsByWeapon;
	private int battleCount;
	private int longestBattleLength;
	
	public BattleStatistics() {
		killsByCreatureID = new CounterMap<ID>();
		killsByCreatureType = new CounterMap<String>();
		killsByWeapon = new CounterMap<ID>();
	}
	
	public void addBattle(Creature attacker, Creature defender, boolean attackerWon, int turns) {
		if (attackerWon) {
			killsByCreatureType.incrementCounter(defender.getType());
			killsByCreatureID.incrementCounter(defender.getID());
			Item weapon = attacker.getWeapon();
			killsByWeapon.incrementCounter(weapon != null ? weapon.getID() : Constants.UNARMED_ID);
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
	
	public CounterMap<String> getKillsByCreatureType() {
		return killsByCreatureType;
	}

	public CounterMap<ID> getKillsByCreatureID() {
		return killsByCreatureID;
	}
	
	public CounterMap<ID> getKillsByWeapon() {
		return killsByWeapon;
	}

}
