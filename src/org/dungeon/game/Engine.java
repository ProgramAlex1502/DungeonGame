package org.dungeon.game;

import java.awt.Color;
import java.util.Random;

import org.dungeon.achievements.Achievement;
import org.dungeon.entity.creatures.Creature;
import org.dungeon.entity.creatures.Hero;
import org.dungeon.entity.items.ItemFactory;
import org.dungeon.io.IO;
import org.dungeon.stats.CauseOfDeath;
import org.dungeon.stats.ExplorationStatistics;
import org.dungeon.util.Constants;
import org.dungeon.util.Percentage;

public class Engine {
	
	public static final Random RANDOM = new Random();
	private static final int WALK_BLOCKED = 2;
	private static final int WALK_SUCCESS = 200;
	
	public static boolean roll(Percentage chance) {
		return chance.toDouble() > RANDOM.nextDouble();
	}
	
	public static boolean roll(double chance) {
		return roll(new Percentage(chance));
	}
	
	public static void refresh() {
		refreshAchievements();
		refreshSpawners();
		refreshItems();
	}
	
	private static void refreshAchievements() {
		Hero hero = Game.getGameState().getHero();
		for (Achievement achievement : GameData.ACHIEVEMENTS.values()) {
			achievement.update(hero);
		}
	}
	
	public static void refreshSpawners() {
		Game.getGameState().getHeroLocation().refreshSpawners();
	}
	
	private static void refreshItems() {
		Game.getGameState().getHeroLocation().getInventory().refreshItems();
		Game.getGameState().getHero().getInventory().refreshItems();
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
		if (world.getLocation(destinationPoint).isBlocked(dir.invert()) || world.getLocation(point).isBlocked(dir)) {
			IO.writeString("You cannot go " + dir + ".");
			return WALK_BLOCKED;
		}
		Location destination = gameState.getWorld().moveHero(dir);
		refreshSpawners();
		hero.setLocation(destination);
		hero.look(dir.invert());
		ExplorationStatistics explorationStatistics = gameState.getStatistics().getExplorationStatistics();
		explorationStatistics.addVisit(destinationPoint, world.getLocation(destinationPoint).getID());
		return WALK_SUCCESS;
	}
	
	public static int battle(Hero hero, Creature foe) {
		if (hero == foe) {
			IO.writeString("You cannot attempt suicide.");
			return 0;
		}
		CauseOfDeath causeOfDeath = null;
		
		int turns = 0;
		while (hero.isAlive() && foe.isAlive()) {
			causeOfDeath = hero.hit(foe);
			hero.getSkillRotation().refresh();
			foe.getSkillRotation().refresh();
			turns++;
			
			if (foe.isAlive()) {
				foe.hit(hero);
				hero.getSkillRotation().refresh();
				foe.getSkillRotation().refresh();
				turns++;
			}
		}
		Creature survivor;
		Creature defeated;
		if (hero.isAlive()) {
			survivor = hero;
			defeated = foe;
		} else {
			survivor = foe;
			defeated = hero;
		}
		
		IO.writeString(survivor.getName() + " managed to kill ."+ defeated.getName() + ".", Color.CYAN);
		
		if (hero == survivor) {
			Game.getGameState().getStatistics().getBattleStatistics().addBattle(hero, causeOfDeath, turns);
			Game.getGameState().getStatistics().getExplorationStatistics().addKill(Game.getGameState().getHeroPosition());
			battleCleanup(survivor, defeated);
		}
		
		return turns;
	}
	
	private static void battleCleanup(Creature survivor, Creature defeated) {
		Location defeatedLocation = defeated.getLocation();
		defeatedLocation.removeCreature(defeated);
		if (defeated.hasTag(Creature.Tag.CORPSE)) {
			defeatedLocation.addItem(ItemFactory.makeCorpse(defeated, defeatedLocation.getWorld().getWorldDate()));			
		}
		defeated.dropEverything();
		survivor.getSkillRotation().restartRotation();
	}

}
