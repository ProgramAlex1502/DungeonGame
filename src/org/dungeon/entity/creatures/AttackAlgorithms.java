package org.dungeon.entity.creatures;

import java.util.HashMap;
import java.util.Map;

import org.dungeon.entity.items.Item;
import org.dungeon.game.ID;
import org.dungeon.game.Random;
import org.dungeon.skill.Skill;
import org.dungeon.stats.CauseOfDeath;
import org.dungeon.stats.TypeOfCauseOfDeath;
import org.dungeon.util.Math;
import org.dungeon.util.Percentage;

final class AttackAlgorithms {
	
	private static final ID UNARMED_ID = new ID("UNARMED");
	private static final Map<AttackAlgorithmID, AttackAlgorithm> ATTACK_ALGORITHM_MAP = new HashMap<AttackAlgorithmID, AttackAlgorithm>();
	
	static {
		final double BAT_CRITICAL_MAXIMUM_LUMINOSITY = 0.5;
		final double BAT_HIT_RATE_MAX_LUMINOSITY = 0.9;
		final double BAT_HIT_RATE_MIN_LUMINOSITY = 0.1;
		registerAttackAlgorithm(AttackAlgorithmID.BAT, new AttackAlgorithm() {
			@Override
			public CauseOfDeath renderAttack(Creature attacker, Creature defender) {
				Percentage luminosity = attacker.getLocation().getLuminosity();
				double hitRate = Math.weightedAverage(BAT_HIT_RATE_MIN_LUMINOSITY, BAT_HIT_RATE_MAX_LUMINOSITY, luminosity);
				if (Random.roll(hitRate)) {
					int hitDamage = attacker.getAttack();
					boolean criticalHit = luminosity.toDouble() <= BAT_CRITICAL_MAXIMUM_LUMINOSITY;
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
		registerAttackAlgorithm(AttackAlgorithmID.BEAST, new AttackAlgorithm() {
			@Override
			public CauseOfDeath renderAttack(Creature attacker, Creature defender) {
				if (Random.roll(BEAST_HIT_RATE)) {
					int hitDamage = attacker.getAttack();
					boolean healthStateChanged = defender.takeDamage(hitDamage);
					AttackAlgorithmIO.printInflictedDamage(attacker, hitDamage, defender, false, healthStateChanged);
				} else {
					AttackAlgorithmIO.printMiss(attacker);
				}
				return null;
			}
		});
		
		registerAttackAlgorithm(AttackAlgorithmID.CRITTER, new AttackAlgorithm() {
			@Override
			public CauseOfDeath renderAttack(Creature attacker, Creature defender) {
				AttackAlgorithmIO.writeCritterAttackMessage(attacker);
				return null;
			}
		});
		
		registerAttackAlgorithm(AttackAlgorithmID.DUMMY, new AttackAlgorithm() {
			@Override
			public CauseOfDeath renderAttack(Creature attacker, Creature defender) {
				AttackAlgorithmIO.writeDummyAttackMessage(attacker);
				return null;
			}
		});
		
		final double ORC_UNARMED_HIT_RATE = 0.95;
		final double ORC_MIN_CRITICAL_CHANCE = 0.1;
		final double ORC_MAX_CRITICAL_CHANCE = 0.5;
		registerAttackAlgorithm(AttackAlgorithmID.ORC, new AttackAlgorithm() {
			@Override
			public CauseOfDeath renderAttack(Creature attacker, Creature defender) {
				Item weapon = attacker.getWeapon();
				int hitDamage;
				Percentage healthiness = new Percentage(attacker.getCurHealth() / (double) attacker.getMaxHealth());
				double criticalChance = Math.weightedAverage(ORC_MIN_CRITICAL_CHANCE, ORC_MAX_CRITICAL_CHANCE, healthiness);
				boolean criticalHit = Random.roll(criticalChance);
				if (weapon != null && !weapon.isBroken()) {
					if (weapon.rollForHit()) {
						hitDamage = weapon.getWeaponComponent().getDamage() + attacker.getAttack();
						weapon.decrementIntegrityByHit();
					} else {
						AttackAlgorithmIO.printMiss(attacker);
						return null;
					}
				} else {
					if (Random.roll(ORC_UNARMED_HIT_RATE)) {
						hitDamage = attacker.getAttack();
					} else {
						AttackAlgorithmIO.printMiss(attacker);
						return null;
					}
				}
				if (criticalHit) {
					hitDamage *= 2;
				}
				boolean healthStateChanged = defender.takeDamage(hitDamage);
				AttackAlgorithmIO.printInflictedDamage(attacker, hitDamage, defender, criticalHit, healthStateChanged);
				if (weapon != null && weapon.isBroken()) {
					AttackAlgorithmIO.printWeaponBreak(weapon);
				}
				return null;
			}
		});
		
		final double UNDEAD_UNARMED_HIT_RATE = 0.85;
		registerAttackAlgorithm(AttackAlgorithmID.UNDEAD, new AttackAlgorithm() {
			@Override
			public CauseOfDeath renderAttack(Creature attacker, Creature defender) {
				Item weapon = attacker.getWeapon();
				int hitDamage;
				if (weapon != null && !weapon.isBroken()) {
					if (weapon.rollForHit()) {
						hitDamage = weapon.getWeaponComponent().getDamage() + attacker.getAttack();
						weapon.decrementIntegrityByHit();
					} else {
						AttackAlgorithmIO.printMiss(attacker);
						return null;
					}
				} else {
					if (Random.roll(UNDEAD_UNARMED_HIT_RATE)) {
						hitDamage = attacker.getAttack();
					} else {
						AttackAlgorithmIO.printMiss(attacker);
						return null;
					}
				}
				boolean healthStateChanged = defender.takeDamage(hitDamage);
				AttackAlgorithmIO.printInflictedDamage(attacker, hitDamage, defender, false, healthStateChanged);
				if (weapon != null && weapon.isBroken()) {
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
		registerAttackAlgorithm(AttackAlgorithmID.HERO, new AttackAlgorithm() {
			@Override
			public CauseOfDeath renderAttack(Creature attacker, Creature defender) {
				CauseOfDeath causeOfDeath;
				if (attacker.getSkillRotation().hasReadySkill()) {
					Skill skill = attacker.getSkillRotation().getNextSkill();
					skill.cast(attacker, defender);
					causeOfDeath = new CauseOfDeath(TypeOfCauseOfDeath.SKILL, skill.getID());
				} else {
					Item weapon = attacker.getWeapon();
					boolean criticalHit;
					int hitDamage;
					if (weapon != null && !weapon.isBroken()) {
						if (weapon.rollForHit()) {
							hitDamage = weapon.getWeaponComponent().getDamage() + attacker.getAttack();
							criticalHit = Random.roll(HERO_CRITICAL_CHANCE);
							weapon.decrementIntegrityByHit();
							causeOfDeath = new CauseOfDeath(TypeOfCauseOfDeath.WEAPON, weapon.getID());
						} else {
							AttackAlgorithmIO.printMiss(attacker);
							return null;
						}
					} else {
						hitDamage = attacker.getAttack();
						criticalHit = Random.roll(HERO_CRITICAL_CHANCE_UNARMED);
						causeOfDeath = new CauseOfDeath(TypeOfCauseOfDeath.UNARMED, UNARMED_ID);
					}
					if (criticalHit) {
						hitDamage *= 2;
					}
					boolean healthStateChanged = defender.takeDamage(hitDamage);
					AttackAlgorithmIO.printInflictedDamage(attacker, hitDamage, defender, criticalHit, healthStateChanged);
					if (weapon != null && weapon.isBroken()) {
						AttackAlgorithmIO.printWeaponBreak(weapon);
					}
				}
				return causeOfDeath;
			}
		});
		
		validateMap();
	}
	
	private AttackAlgorithms() {
		throw new AssertionError();
	}
	
	private static void validateMap() {
		for (AttackAlgorithmID id : AttackAlgorithmID.values()) {
			if (!ATTACK_ALGORITHM_MAP.containsKey(id)) {
				throw new AssertionError(id + " is not mapped to an AttackAlgorithm!");
			}
		}
	}
	
	private static void registerAttackAlgorithm(AttackAlgorithmID id, AttackAlgorithm algorithm) {
		if (ATTACK_ALGORITHM_MAP.containsKey(id)) {
			throw new IllegalStateException("There is an AttackAlgorithm already defined for this AttackAlgorithmID!");
		}
		ATTACK_ALGORITHM_MAP.put(id, algorithm);
	}
	
	public static CauseOfDeath renderAttack(Creature attacker, Creature defender) {
		return ATTACK_ALGORITHM_MAP.get(attacker.getAttackAlgorithmID()).renderAttack(attacker, defender);
	}

}
