package org.dungeon.creatures;

import org.dungeon.stats.CauseOfDeath;

interface AttackAlgorithm {
	
	CauseOfDeath renderAttack(Creature attacker, Creature defender);
	
}