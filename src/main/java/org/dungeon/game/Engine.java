package main.java.org.dungeon.game;

import java.awt.Color;
import java.util.Random;

import main.java.org.dungeon.achievements.Achievement;
import main.java.org.dungeon.creatures.Creature;
import main.java.org.dungeon.creatures.Hero;
import main.java.org.dungeon.io.IO;
import main.java.org.dungeon.stats.ExplorationStatistics;
import main.java.org.dungeon.util.Constants;

public class Engine {
	
	public static final Random RANDOM = new Random();
	
	public static void refresh() {
		refreshAchievements();
		refreshSpawners();
	}
	
	private static void refreshAchievements() {
		Hero hero = Game.getGameState().getHero();
		
		for (Achievement a : GameData.ACHIEVEMENTS.values()) {
			a.update(hero);
		}
	}
	
	private static void refreshSpawners() {
		Game.getGameState().getHeroLocation().refreshSpawners();
	}
	
	public static int parseHeroWalk(IssuedCommand issuedCommand) {
		if (issuedCommand.hasArguments()) {
			for (Direction dir : Direction.values()) {
				if (dir.equalsIgnoreCase(issuedCommand.getFirstArgument())) {
					return heroWalk(dir);
				}
			}
			IO.writeString(Constants.INVALID_INPUT);
		} else {
			IO.writeString("To where?", Color.ORANGE);
		}
		
		return 0;
	}
	
	private static int heroWalk(Direction dir) {
		GameState gameState = Game.getGameState();
		World world = gameState.getWorld();
		Point point = gameState.getHeroPosition();
		Hero hero = gameState.getHero();
		Point destinationPoint = new Point(gameState.getHeroPosition(), dir);
		
		if(world.getLocation(destinationPoint).isBlocked(dir.invert()) || world.getLocation(point).isBlocked(dir)) {
			IO.writeString("You cannot go " + dir + ".");
			return TimeConstants.WALK_BLOCKED;
		}
		
		Location destination = gameState.getWorld().moveHero(dir);
		refreshSpawners();
		hero.setLocation(destination);
		hero.look(dir.invert());
		ExplorationStatistics explorationStatistics = gameState.getStatistics().getExplorationStatistics();
		
		explorationStatistics.addVisit(destinationPoint, world.getLocation(destinationPoint).getID());
		return TimeConstants.WALK_SUCCESS;
	}
	
	public static int battle(Hero attacker, Creature defender) {
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
			attacker.getSkillRotation().refresh();
			defender.getSkillRotation().refresh();
			turns++;
			if (defender.isAlive()) {
				defender.hit(attacker);
				attacker.getSkillRotation().refresh();
				defender.getSkillRotation().refresh();
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

		boolean attackerWon = attacker == survivor;
		Game.getGameState().getStatistics().getBattleStatistics().addBattle(attacker, defender, attackerWon, turns);
		Game.getGameState().getStatistics().getExplorationStatistics().addKill(Game.getGameState().getHeroPosition());

		
		battleCleanup(survivor, defeated);
		
		return turns;
	}
	
	private static void battleCleanup(Creature survivor, Creature defeated) {
		defeated.getLocation().removeCreature(defeated);
		survivor.getSkillRotation().restartRotation();
	}
}
