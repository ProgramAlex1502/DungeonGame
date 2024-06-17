package main.java.org.dungeon.creatures;

import java.util.ArrayList;

import org.joda.time.DateTime;

import main.java.org.dungeon.achievements.AchievementTracker;
import main.java.org.dungeon.counters.BattleStatistics;
import main.java.org.dungeon.counters.ExplorationLog;
import main.java.org.dungeon.game.Direction;
import main.java.org.dungeon.game.Engine;
import main.java.org.dungeon.game.Game;
import main.java.org.dungeon.game.IssuedCommand;
import main.java.org.dungeon.game.Location;
import main.java.org.dungeon.game.PartOfDay;
import main.java.org.dungeon.game.Point;
import main.java.org.dungeon.game.TimeConstants;
import main.java.org.dungeon.game.World;
import main.java.org.dungeon.io.IO;
import main.java.org.dungeon.items.CreatureInventory;
import main.java.org.dungeon.items.Item;
import main.java.org.dungeon.utils.Constants;
import main.java.org.dungeon.utils.Utils;

public class Hero extends Creature {
	
	private final double minimumLuminosity = 0.3;
	
	private final DateTime dateOfBirth;
	private final ExplorationLog explorationLog;
	private final BattleStatistics battleStatistics;
	private final AchievementTracker achievementTracker;

	public Hero(String name) {
		super(makeHeroBlueprint(name));
		setInventory(new CreatureInventory(this, 3));
		dateOfBirth = new DateTime(1952, 6, 4, 8, 32);
		explorationLog = new ExplorationLog();
		battleStatistics = new BattleStatistics();
		achievementTracker = new AchievementTracker();
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
	
	public ExplorationLog getExplorationLog() {
		return explorationLog;
	}
	
	public AchievementTracker getAchievementTracker() {
		return achievementTracker;
	}
	
	private DateTime getDateOfBirth() {
		return dateOfBirth;
	}
	
	private boolean isCompletelyHealed() {
		return getMaxHealth() == getCurHealth();
	}
	
	public int rest() {
		if (getCurHealth() >= (int) (0.6 * getMaxHealth())) {
			IO.writeString("You are already rested.");
			return 0;
		} else {
			double fractionHealed = 0.6 - (double) getCurHealth() / (double) getMaxHealth();
			IO.writeString("Resting...");
			setCurHealth((int) 0.6 * getMaxHealth());
			IO.writeString("You feel rested.");
			return (int) (TimeConstants.REST_COMPLETE * fractionHealed);
		}
	}
	
	public int sleep() {
		int seconds = 0;
		
		World world = getLocation().getWorld();
		PartOfDay pod = world.getPartOfDay();
		
		if (pod == PartOfDay.EVENING || pod == PartOfDay.MIDNIGHT || pod == PartOfDay.NIGHT) {
			IO.writeString("You fall asleep.");
			seconds = PartOfDay.getSecondsToNext(world.getWorldDate(), PartOfDay.DAWN);
			IO.writeString("You wake up.");
		} else {
			IO.writeString("You can only sleep at night.");
		}
		
		return seconds;
	}
	
	public void look() {
		Location location = getLocation();
		
		IO.writeString(location.getName());
		IO.writeNewLine();
		
		if (canSee()) {
			lookAdjacentLocations();
			if (location.getCreatureCount() == 1) {
				if (Engine.RANDOM.nextBoolean()) {
					IO.writeString("You do not see anyone here.");
				} else {
					IO.writeString("Only you are in this location.");
				}
			} else {
				String curName;
				int curCount;
				ArrayList<String> alreadyListedCreatures = new ArrayList<String>();
				alreadyListedCreatures.add(getName());
				for (Creature creature : location.getCreatures()) {
					curName = creature.getName();
					if (!alreadyListedCreatures.contains(curName)) {
						alreadyListedCreatures.add(curName);
						curCount = location.getCreatureCount(creature.getId());
						if (curCount > 1) {
							IO.writeKeyValueString(curName, Integer.toString(curCount));
						} else {
							IO.writeString(curName);
						}
					}
				}
			}
			
			if (getLocation().getItemCount() != 0) {
				IO.writeNewLine();
				
				for(Item curItem : getLocation().getItemList()) {
					IO.writeString(curItem.toListEntry());
				}
			}
		} else {
			IO.writeString(Constants.CANT_SEE_ANYTHING);
		}
	}
	
	private void lookAdjacentLocations() {
		World world = Game.getGameState().getWorld();
		Point pos = Game.getGameState().getHeroPosition();
		
		StringBuilder stringBuilder = new StringBuilder(140);
		
		for(Direction dir : Direction.values()) {
			stringBuilder.append("To ");
			stringBuilder.append(dir);
			stringBuilder.append(" you see ");
			stringBuilder.append(world.getLocation(new Point(pos, dir)).getName());
			stringBuilder.append(".\n");
		}
		
		IO.writeString(stringBuilder.toString());
		IO.writeNewLine();
	}
	
	public boolean canSee() {
		return getLocation().getLuminosity() >= minimumLuminosity;
	}
	
	Item selectLocationItem(IssuedCommand issuedCommand) {
		if (issuedCommand.hasArguments()) {
			return getLocation().getInventory().findItem(issuedCommand.getArguments());
		} else {
			Utils.printMissingArgumentsMessage();
			return null;
		}
	}

}
