package org.dungeon.game;

import java.awt.Color;

import org.dungeon.commands.IssuedCommand;
import org.dungeon.date.Date;
import org.dungeon.date.DungeonTimeUnit;
import org.dungeon.entity.creatures.Creature;
import org.dungeon.entity.creatures.Hero;
import org.dungeon.entity.items.ItemFactory;
import org.dungeon.io.IO;
import org.dungeon.stats.CauseOfDeath;
import org.dungeon.stats.ExplorationStatistics;
import org.dungeon.util.Constants;

public class Engine {
	
	private static final int BATTLE_TURN_DURATION = 30;
	private static final int WALK_BLOCKED = 2;
	private static final int WALK_SUCCESS = 200;
	
	public static void refresh() {
		refreshAchievements();
		refreshSpawners();
		refreshItems();
	}
	
	private static void refreshAchievements() {
		Hero hero = Game.getGameState().getHero();
		hero.getAchievementTracker().update(GameData.ACHIEVEMENTS.values());
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
		int duration = turns * BATTLE_TURN_DURATION;
		
		if (hero == survivor) {
			if (causeOfDeath == null) {
				throw new AssertionError();
			}
			Date date = Game.getGameState().getWorld().getWorldDate().plus(duration, DungeonTimeUnit.SECOND);
			PartOfDay partOfDay = PartOfDay.getCorrespondingConstant(date);
			Game.getGameState().getStatistics().getBattleStatistics().addBattle(hero, causeOfDeath, partOfDay);
			Game.getGameState().getStatistics().getExplorationStatistics().addKill(Game.getGameState().getHeroPosition());
			battleCleanup(survivor, defeated);
		}
		
		return duration;
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
