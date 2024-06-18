package main.java.org.dungeon.creatures;

import java.awt.Color;
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
import main.java.org.dungeon.items.FoodComponent;
import main.java.org.dungeon.items.Item;
import main.java.org.dungeon.utils.Constants;
import main.java.org.dungeon.utils.SelectionResult;
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
	
	Item selectInventoryItem(IssuedCommand issuedCommand) {
		if (issuedCommand.hasArguments()) {
			return getInventory().findItem(issuedCommand.getArguments());
		} else {
			Utils.printMissingArgumentsMessage();
			return null;
		}
	}
	
	Item selectLocationItem(IssuedCommand issuedCommand) {
		if (issuedCommand.hasArguments()) {
			return getLocation().getInventory().findItem(issuedCommand.getArguments());
		} else {
			Utils.printMissingArgumentsMessage();
			return null;
		}
	}
	
	public int attackTarget(IssuedCommand issuedCommand) {
		if (canSee()) {
			Creature target = selectTarget(issuedCommand);
			if (target != null) {
				return Engine.battle(this, target) * TimeConstants.BATTLE_TURN_DURATION;
			}
		} else {
			IO.writeString("It is too dark to find your target.");
		}
		
		return 0;
	}
	
	public Creature selectTarget(IssuedCommand issuedCommand) {
		if (issuedCommand.hasArguments()) {
			return findCreature(issuedCommand.getArguments());
		} else {
			if (Engine.RANDOM.nextBoolean()) {
				IO.writeString("Attack what?", Color.ORANGE);
			} else {
				IO.writeString("You must specify a target.", Color.ORANGE);
			}
			return null;
		}
	}
	
	public Creature findCreature(String[] tokens) {
		SelectionResult<Creature> result = Utils.selectFromList(getLocation().getCreatures(), tokens);
		if (result.size() == 0) {
			IO.writeString("Creature not found.");
		} else if (result.size() == 1 || result.getDifferentNames() == 1) {
			return result.getMatch(0);
		} else if (result.getDifferentNames() == 2 && result.hasName(getName())) {
			return result.getMatch(0).getName().equals(getName()) ? result.getMatch(1) : result.getMatch(0);
		} else {
			Utils.printAmbiguousSelectionMessage();
		}
		
		return null;
	}
	
	public void pickItem(IssuedCommand issuedCommand) {
		if (canSee()) {
			Item selectedItem = selectLocationItem(issuedCommand);
			if (selectedItem != null) {
				if (getInventory().isFull()) {
					IO.writeString(Constants.INVENTORY_FULL);
				} else {
					getInventory().addItem(selectedItem);
					getLocation().removeItem(selectedItem);
				}
			}
		} else {
			IO.writeString("It is too dark for you too see anything.");
		}
	}
	
	public void parseEquip(IssuedCommand issuedCommand) {
		Item selectedItem = selectInventoryItem(issuedCommand);
		
		if (selectedItem != null) {
			if (selectedItem.isWeapon()) {
				equipWeapon(selectedItem);
			} else {
				IO.writeString("You cannot equip that.");
			}
		}
	}
	
	public void dropItem(IssuedCommand issuedCommand) {
		Item selectedItem = selectInventoryItem(issuedCommand);
		
		if (selectedItem != null) {
			if (selectedItem == getWeapon()) {
				unequipWeapon();
			}
			
			getInventory().removeItem(selectedItem);
			getLocation().addItem(selectedItem);
			IO.writeString("Dropped " + selectedItem.getName() + ".");
		}
	}
	
	public void printInventory() {
		IO.writeString("Items: " + getInventory().getItemCount() + "/" + getInventory().getLimit(), Color.CYAN);
		getInventory().printItems();
	}
	
	public void eatItem(IssuedCommand issuedCommand) {
		Item selectedItem = selectInventoryItem(issuedCommand);
		
		if (selectedItem != null) {
			if (selectedItem.isFood()) {
				FoodComponent food = selectedItem.getFoodComponent();
				double remainingBites = selectedItem.getCurIntegrity() / (double) food.getIntegrityDecrementOnEat();
				if (remainingBites >= 1.0) {
					addHealth(food.getNutrition());
				} else {
					addHealth((int) (food.getNutrition() * remainingBites));
				}
				
				selectedItem.decrementIntegrity(food.getIntegrityDecrementOnEat());
				
				if (selectedItem.isBroken() && !selectedItem.isRepairable()) {
					IO.writeString("You ate " + selectedItem.getName() + ".");
					getInventory().removeItem(selectedItem);
				} else {
					IO.writeString("You ate a bit of " + selectedItem.getName() + ".");
				}
				
				if (isCompletelyHealed()) {
					IO.writeString("You are completely healed.");
				}
			} else {
				IO.writeString("You can only eat food.");
			}
		}
	}
	
	public void destroyItem(IssuedCommand issuedCommand) {
		Item target;
		
		if (issuedCommand.hasArguments()) {
			target = getLocation().getInventory().findItem(issuedCommand.getArguments());
		} else {
			Utils.printMissingArgumentsMessage();
			target = null;
		}
		
		if (target != null) {
			if (target.isRepairable()) {
				if (!target.isBroken()) {
					target.setCurIntegrity(0);
					IO.writeString(getName() + " crashed " + target.getName() + ".");
				}
			} else {
				getLocation().removeItem(target);
				IO.writeString(getName() + " destroyed " + target.getName() + ".");
			}
		}
	}
	
	boolean hasClock() {
		for (Item item : getInventory().getItems()) {
			if (item.isClock()) {
				return true;
			}
		}
		
		return false;
	}
	
	Item getClock() {
		for (Item item : getInventory().getItems()) {
			if (item.isClock()) {
				return item;
			}
		}
		
		return null;
	}
	
	void equipWeapon(Item weapon) {
		if (hasWeapon()) {
			if (getWeapon() == weapon) {
				IO.writeString(getName() + " is already equipping " + weapon.getName() + ".");
				return;
			} else {
				unequipWeapon();
			}
		}
		
		this.setWeapon(weapon);
		IO.writeString(getName() + " equipped " + weapon.getName() + ".");
	}
	
	public void unequipWeapon() {
		if (hasWeapon()) {
			IO.writeString(getName() + " unequipped " + getWeapon().getName() + ".");
			setWeapon(null);
		} else {
			IO.writeString(Constants.NOT_EQUIPPING_A_WEAPON);
		}
	}
	
	public void printHeroStatus() {
		IO.writeString(getName());
		
		if (Game.getGameState().isUsingBars()) {
			IO.writeNamedBar("Health", (double) getCurHealth() / getMaxHealth(), Constants.HEALTH_BAR_COLOR);
		} else {
			IO.writeKeyValueString("Health", String.format("%d/%d", getCurHealth(), getMaxHealth()));
		}
		IO.writeKeyValueString("Attack", Integer.toString(getAttack()));
	}
	
	public void printWeaponStatus() {
		if (hasWeapon()) {
			Item heroWeapon = getWeapon();
			IO.writeString(heroWeapon.getQualifiedName());
			IO.writeKeyValueString("Damage", Integer.toString(heroWeapon.getDamage()));
		} else {
			IO.writeString(Constants.NOT_EQUIPPING_A_WEAPON);
		}
	}
	
	public void printAllStatus() {
		printHeroStatus();
		if (getWeapon() != null) {
			printWeaponStatus();
		}
	}

}
