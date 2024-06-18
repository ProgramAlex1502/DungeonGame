package main.java.org.dungeon.game;

import java.awt.Color;
import java.util.Random;

import main.java.org.dungeon.creatures.Creature;
import main.java.org.dungeon.creatures.Hero;
import main.java.org.dungeon.io.IO;
import main.java.org.dungeon.utils.Constants;

public class Engine {

	//TODO: finish Engine class
	
	public static final Random RANDOM = new Random();
	
	public static int battle(Creature attacker, Creature defender) {
		if (attacker == defender) {
			if(RANDOM.nextBoolean()) {
				IO.writeString(Constants.SUICIDE_ATTEMPT_1);
			} else {
				IO.writeString(Constants.SUICIDE_ATTEMPT_2);
			}
			return 0;
		}
		
		int turns = 0;
		
		while (attacker.isAlive() && defender.isAlive()) {
			attacker.hit(defender);
			turns++;
			if (defender.isAlive()) {
				defender.hit(attacker);
				turns++;
			}
		}
		
		Creature survivor;
		Creature defeated;
		
		if (attacker.isAlive()) {
			survivor = attacker;
			defeated = defender;
		} else {
			survivor = defender;
			defeated = attacker;
		}
		
		IO.writeString(String.format("%s managed to kill %s.", survivor.getName(), defeated.getName()), Color.CYAN);
		
		if (attacker instanceof Hero) {
			Hero hero = (Hero) attacker;
			hero.getBattleStatistics().addBattle(attacker, defender, attacker == survivor, turns);
			hero.getExplorationLog().addKill(Game.getGameState().getHeroPosition());
		}
		
		battleCleanup(survivor, defeated);
		
		return turns;
	}
	
	private static void battleCleanup(Creature survivor, Creature defeated) {
		survivor.getLocation().removeCreature(defeated);
	}
}
