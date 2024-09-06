package org.dungeon.entity.creatures;

import java.awt.Color;

import org.dungeon.entity.items.Item;
import org.dungeon.game.Engine;
import org.dungeon.io.IO;
import org.dungeon.util.Constants;

class AttackAlgorithmIO {
	
	static void printInflictedDamage(Creature attacker, int hitDamage, Creature defender, boolean criticalHit, boolean healthStateChanged) {
		String s = String.format("%s inflicted %d damage points to %s", attacker.getName(), hitDamage, defender.getName());
		s += criticalHit ? " with a critical hit." : ".";
		if (healthStateChanged) {
			HealthState currentHealthState = HealthState.getHealthState(defender.getCurHealth(), defender.getMaxHealth());
			s += String.format(" It looks %s.", currentHealthState.toString().toLowerCase());
		}
		IO.writeBattleString(s, attacker.getID().equals(Constants.HERO_ID) ? Color.GREEN : Color.RED);
	}
	
	static void printMiss(Creature attacker) {
		IO.writeBattleString(attacker.getName() + " missed.", Color.YELLOW);
	}
	
	static void printWeaponBreak(Item weapon) {
		IO.writeString(weapon.getName() + " broke!", Color.RED);
	}
	
	public static void writeCritterAttackMessage(Creature attacker) {
		if (Engine.RANDOM.nextBoolean()) {
			IO.writeBattleString(attacker.getName() + " does nothing.", Color.YELLOW);
		} else {
			IO.writeBattleString(attacker.getName() + " tries to run away.", Color.YELLOW);
		}
	}
	
	public static void writeDummyAttackMessage(Creature attacker) {
		IO.writeBattleString(attacker.getName() + " stands still.", Color.YELLOW);
	}

}
