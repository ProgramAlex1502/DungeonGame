package org.dungeon.creatures;

import java.util.HashMap;
import java.util.Map;

import org.dungeon.game.Engine;
import org.dungeon.game.ID;
import org.dungeon.io.DLogger;
import org.dungeon.items.Item;
import org.dungeon.skill.Skill;
import org.dungeon.stats.CauseOfDeath;
import org.dungeon.stats.TypeOfCauseOfDeath;
import org.dungeon.util.Constants;

public final class AttackAlgorithms {
	
	static final Map<ID, AttackAlgorithm> ATTACK_ALGORITHM_MAP = new HashMap<ID, AttackAlgorithm>();
	
	static {
		final double BAT_CRITICAL_MAXIMUM_LUMINOSITY = 0.5;
		registerAttackAlgorithm("BAT", new AttackAlgorithm() {
			@Override
			public CauseOfDeath renderAttack(Creature attacker, Creature defender) {
				double luminosity = attacker.getLocation().getLuminosity().toDouble();
				if (Engine.roll(0.9 - luminosity / 2)) {
					int hitDamage = attacker.getAttack();
					boolean criticalHit = luminosity <= BAT_CRITICAL_MAXIMUM_LUMINOSITY;
					if (criticalHit) {
						hitDamage *= 2;
					}
					boolean healthStateChanged = defender.takeDamage(hitDamage);
					AttackAlgorithmIO.printInflictedDamage(attacker, hitDamage, defender, criticalHit, healthStateChanged);
				} else {
					AttackAlgorithmIO.printMiss(attacker);
				}
				return null;
			}
		});
		
		final double BEAST_HIT_RATE = 0.9;
		registerAttackAlgorithm("BEAST", new AttackAlgorithm() {
			@Override
			public CauseOfDeath renderAttack(Creature attacker, Creature defender) {
				if (Engine.roll(BEAST_HIT_RATE)) {
					int hitDamage = attacker.getAttack();
					boolean healthStateChanged = defender.takeDamage(hitDamage);
					AttackAlgorithmIO.printInflictedDamage(attacker, hitDamage, defender, false, healthStateChanged);
				} else {
					AttackAlgorithmIO.printMiss(attacker);
				}
				return null;
			}
		});
		
		registerAttackAlgorithm("CRITTER", new AttackAlgorithm() {
			@Override
			public CauseOfDeath renderAttack(Creature attacker, Creature defender) {
				AttackAlgorithmIO.writeCritterAttackMessage(attacker);
				return null;
			}
		});
		
		registerAttackAlgorithm("DUMMY", new AttackAlgorithm() {
			@Override
			public CauseOfDeath renderAttack(Creature attacker, Creature defender) {
				AttackAlgorithmIO.writeDummyAttackMessage(attacker);
				return null;
			}
		});
		
		final double UNDEAD_UNARMED_HIT_RATE = 0.85;
		registerAttackAlgorithm("UNDEAD", new AttackAlgorithm() {
			@Override
			public CauseOfDeath renderAttack(Creature attacker, Creature defender) {
				Item weapon = attacker.getWeapon();
				int hitDamage;
				boolean weaponBroke = false;
				if (weapon != null && !weapon.isBroken()) {
					if (weapon.rollForHit()) {
						hitDamage = weapon.getWeaponComponent().getDamage() + attacker.getAttack();
						weapon.decrementIntegrityByHit();
						weaponBroke = weapon.isBroken();
					} else {
						AttackAlgorithmIO.printMiss(attacker);
						return null;
					}
				} else {
					if (Engine.roll(UNDEAD_UNARMED_HIT_RATE)) {
						hitDamage = attacker.getAttack();
					} else {
						AttackAlgorithmIO.printMiss(attacker);
						return null;
					}
				}
				boolean healthStateChanged = defender.takeDamage(hitDamage);
				AttackAlgorithmIO.printInflictedDamage(attacker, hitDamage, defender, false, healthStateChanged);
				if (weaponBroke) {
					AttackAlgorithmIO.printWeaponBreak(weapon);
					if (!weapon.hasTag(Item.Tag.REPAIRABLE)) {
						attacker.getInventory().removeItem(weapon);
					}
				}
				return null;
			}
		});
		
		final double HERO_CRITICAL_CHANCE = 0.1;
		final double HERO_CRITICAL_CHANCE_UNARMED = 0.05;
		registerAttackAlgorithm("HERO", new AttackAlgorithm() {
			@Override
			public CauseOfDeath renderAttack(Creature attacker, Creature defender) {
				CauseOfDeath causeOfDeath;
				if (attacker.getSkillRotation().hasReadySkill()) {
					Skill skill = attacker.getSkillRotation().getNextSkill();
					skill.cast(attacker, defender);
					causeOfDeath = new CauseOfDeath(TypeOfCauseOfDeath.SKILL, skill.getID());
				} else {
					Item weapon = attacker.getWeapon();
					boolean weaponBroke = false;
					boolean criticalHit;
					int hitDamage;
					if (weapon != null && !weapon.isBroken()) {
						if (weapon.rollForHit()) {
							hitDamage = weapon.getWeaponComponent().getDamage() + attacker.getAttack();
							criticalHit = Engine.roll(HERO_CRITICAL_CHANCE);
							weapon.decrementIntegrityByHit();
							weaponBroke = weapon.isBroken();
							causeOfDeath = new CauseOfDeath(TypeOfCauseOfDeath.WEAPON, weapon.getID());
						} else {
							AttackAlgorithmIO.printMiss(attacker);
							return null;
						}
					} else {
						hitDamage = attacker.getAttack();
						criticalHit = Engine.roll(HERO_CRITICAL_CHANCE_UNARMED);
						causeOfDeath = new CauseOfDeath(TypeOfCauseOfDeath.WEAPON, Constants.UNARMED_ID);
					}
					if (criticalHit) {
						hitDamage *= 2;
					}
					boolean healthStateChanged = defender.takeDamage(hitDamage);
					AttackAlgorithmIO.printInflictedDamage(attacker, hitDamage, defender, criticalHit, healthStateChanged);
					if (weaponBroke) {
						AttackAlgorithmIO.printWeaponBreak(weapon);
						if (!weapon.hasTag(Item.Tag.REPAIRABLE)) {
							attacker.getInventory().removeItem(weapon);
						}
					}
				}
				return causeOfDeath;
			}
		});
	}
	
	private AttackAlgorithms() {
		throw new AssertionError();
	}
	
	private static void registerAttackAlgorithm(String name, AttackAlgorithm algorithm) {
		ID id = new ID(name);
		if (ATTACK_ALGORITHM_MAP.containsKey(id)) {
			throw new AssertionError("There is an AttackAlgorithm already defined with the ID produced by this name!");
		}
		ATTACK_ALGORITHM_MAP.put(id, algorithm);
	}
	
	public static CauseOfDeath renderAttack(Creature attacker, Creature defender) {
		AttackAlgorithm attackAlgorithm = ATTACK_ALGORITHM_MAP.get(attacker.getAttackAlgorithmID());
		if (attackAlgorithm != null) {
			return attackAlgorithm.renderAttack(attacker, defender);
		} else {
			DLogger.warning("AttackAlgorithmID does not match any implemented algorithm.");
			return null;
		}
	}

}
