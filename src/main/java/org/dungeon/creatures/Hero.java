package main.java.org.dungeon.creatures;

import org.joda.time.DateTime;

import main.java.org.dungeon.counters.BattleStatistics;
import main.java.org.dungeon.counters.ExplorationLog;
import main.java.org.dungeon.items.CreatureInventory;
import main.java.org.dungeon.utils.Constants;

public class Hero extends Creature {
	
	private final double minimumLuminosity = 0.3;
	
	private final DateTime dateOfBirth;
	private final ExplorationLog explorationLog;
	private final BattleStatistics battleStatistics;

	public Hero(String name) {
		super(makeHeroBlueprint(name));
		setInventory(new CreatureInventory(this, 3));
		dateOfBirth = new DateTime(1952, 6, 4, 8, 32);
		explorationLog = new ExplorationLog();
		battleStatistics = new BattleStatistics();
	}
	
	private static CreatureBlueprint makeHeroBlueprint(String name) {
		CreatureBlueprint heroBlueprint = new CreatureBlueprint();
		heroBlueprint.setId(Constants.HERO_ID);
		heroBlueprint.setName(name);
		heroBlueprint.setType("Hero");
		heroBlueprint.setAttack(4);
		heroBlueprint.setAttackAlgorithmID("HERO");
		heroBlueprint.setMaxHealth(50);
		heroBlueprint.setCurHealth(50);
		return heroBlueprint;
	}
	
	public BattleStatistics getBattleStatistics() {
		return battleStatistics;
	}
	
	
	
	

}
