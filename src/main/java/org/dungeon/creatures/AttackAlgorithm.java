package main.java.org.dungeon.creatures;

import java.awt.Color;

import main.java.org.dungeon.game.Engine;
import main.java.org.dungeon.io.IO;
import main.java.org.dungeon.items.Item;
import main.java.org.dungeon.skill.Skill;
import main.java.org.dungeon.util.Constants;
import main.java.org.dungeon.util.Utils;

class AttackAlgorithm {
	
	private static final double HERO_CRITICAL_CHANCE = 0.1;
	private static final double HERO_CRITICAL_CHANCE_UNARMED = 0.05;
	
	public static void attack(Creature attacker, Creature defender, String algorithmID) {
		if (algorithmID.equals("BAT")) {
			batAttack(attacker, defender);
		} else if (algorithmID.equals("BEAST")) {
			beastAttack(attacker, defender);
		} else if (algorithmID.equals("CRITTER")) {
			critterAttack(attacker);
		} else if (algorithmID.equals("DUMMY")) {
			dummyAttack(attacker);
		} else if (algorithmID.equals("UNDEAD")) {
			undeadAttack(attacker, defender);
		} else if (algorithmID.equals("HERO")) {
			heroAttack(attacker, defender);
		} else {
			throw new IllegalArgumentException("algorithmID does not match any implemented algorithm");
		}
	}
	
	private static void batAttack(Creature attacker, Creature defender) {
		double luminosity = attacker.getLocation().getLuminosity();
		
		if (Utils.roll(0.9 - luminosity / 2)) {
			int hitDamage = attacker.getAttack();
			if (luminosity == 0.0) {
				hitDamage *= 2;
				printInflictedDamage(attacker, hitDamage, defender, true);
			} else {
				printInflictedDamage(attacker, hitDamage, defender, false);
			}
			defender.takeDamage(hitDamage);
		} else {
			printMiss(attacker);
		}
	}
	
	private static void beastAttack(Creature attacker, Creature defender) {
		if (Utils.roll(0.9)) {
			int hitDamage = attacker.getAttack();
			defender.takeDamage(hitDamage);
			printInflictedDamage(attacker, hitDamage, defender, false);
		} else {
			printMiss(attacker);
		}
	}
	
	private static void critterAttack(Creature attacker) {
		if (Engine.RANDOM.nextBoolean()) {
			IO.writeBattleString(attacker.getName() + " does nothing.", Color.YELLOW);
		} else {
			IO.writeBattleString(attacker.getName() + " tries to run away.", Color.YELLOW);
		}
	}
	
	private static void dummyAttack(Creature attacker) {
		IO.writeBattleString(attacker.getName() + " stands still.", Color.YELLOW);
	}
	
	private static void undeadAttack(Creature attacker, Creature defender) {
		Item weapon = attacker.getWeapon();
		int hitDamage;
		
		if (weapon != null && !weapon.isBroken()) {
			if (weapon.rollForHit()) {
				hitDamage = weapon.getDamage() + attacker.getAttack();
				printInflictedDamage(attacker, hitDamage, defender, false);
				weapon.decrementIntegrityByHit();
				if (weapon.isBroken()) {
					printWeaponBreak(weapon);
					if (!weapon.isRepairable()) {
						attacker.getInventory().removeItem(weapon);
					}
				}
			} else {
				printMiss(attacker);
				return;
			}
		} else {
			if (0.85 > Engine.RANDOM.nextDouble()) {
				hitDamage = attacker.getAttack();
				printInflictedDamage(attacker, hitDamage, defender, false);
			} else {
				printMiss(attacker);
				return;
			}
		}
		
		defender.takeDamage(hitDamage);
	}
	
	private static void heroAttack(Creature attacker, Creature defender) {
		Item weapon = attacker.getWeapon();
		int hitDamage;
		
		if (attacker.getSkillRotation().hasReadySkill()) {
			Skill skill = attacker.getSkillRotation().getNextSkill();
			hitDamage = skill.getDamage();
			skill.startCoolDown();
			printSkillCast(attacker, skill, defender);
		} else {
			if (weapon != null && !weapon.isBroken()) {
				if (weapon.rollForHit()) {
					hitDamage = weapon.getDamage() + attacker.getAttack();
					if (Utils.roll(HERO_CRITICAL_CHANCE)) {
						hitDamage *= 2;
						printInflictedDamage(attacker, hitDamage, defender, true);
					} else {
						printInflictedDamage(attacker, hitDamage, defender, false);
					}
					
					weapon.decrementIntegrityByHit();
					
					if (weapon.isBroken()) {
						printWeaponBreak(weapon);
						if (!weapon.isRepairable()) {
							attacker.getInventory().removeItem(weapon);
						}
					}
				} else {
					printMiss(attacker);
					return;
				}
			} else {
				hitDamage = attacker.getAttack();
				
				if (Utils.roll(HERO_CRITICAL_CHANCE_UNARMED)) {
					hitDamage *= 2;
					printInflictedDamage(attacker, hitDamage, defender, true);
				} else {
					printInflictedDamage(attacker, hitDamage, defender, false);
					
				}
			}
		}
		defender.takeDamage(hitDamage);
	}
	
	private static void printWeaponBreak(Item weapon) {
		IO.writeString(weapon.getName() + " broke!", Color.RED);
	}
	
	private static void printInflictedDamage(Creature attacker, int hitDamage, Creature defender, boolean criticalHit) {
		StringBuilder builder = new StringBuilder();
		
		builder.append(attacker.getName());
		builder.append(" inflicted ");
		builder.append(hitDamage);
		builder.append(" damage points to ");
		builder.append(defender.getName());
		
		if (criticalHit) {
			builder.append(" with a critical hit");
		}
		
		builder.append(".");
		IO.writeBattleString(builder.toString(), attacker.getID().equals(Constants.HERO_ID) ? Color.GREEN : Color.RED);
	}
	
	private static void printSkillCast(Creature attacker, Skill skill, Creature defender) {
		String result = attacker.getName() + " casted " + 
				skill.getName() + " and inflicted " + 
				skill.getDamage() + " damage points to " + 
				defender.getName() + ".";
		IO.writeString(result, attacker.getID().equals(Constants.HERO_ID) ? Color.GREEN : Color.RED);
	}
	
	private static void printMiss(Creature attacker) {
		IO.writeBattleString(attacker.getName() + " missed.", Color.YELLOW);
	}

}
