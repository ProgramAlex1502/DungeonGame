package org.dungeon.creatures;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.dungeon.achievements.AchievementTracker;
import org.dungeon.date.Date;
import org.dungeon.date.Period;
import org.dungeon.game.Direction;
import org.dungeon.game.Engine;
import org.dungeon.game.Entity;
import org.dungeon.game.Game;
import org.dungeon.game.GameData;
import org.dungeon.game.ID;
import org.dungeon.game.IssuedCommand;
import org.dungeon.game.Location;
import org.dungeon.game.Name;
import org.dungeon.game.PartOfDay;
import org.dungeon.game.Point;
import org.dungeon.game.QuantificationMode;
import org.dungeon.game.Selectable;
import org.dungeon.game.TimeConstants;
import org.dungeon.game.World;
import org.dungeon.io.IO;
import org.dungeon.io.Sleeper;
import org.dungeon.items.BaseInventory;
import org.dungeon.items.BookComponent;
import org.dungeon.items.CreatureInventory;
import org.dungeon.items.FoodComponent;
import org.dungeon.items.Item;
import org.dungeon.skill.Skill;
import org.dungeon.stats.ExplorationStatistics;
import org.dungeon.util.Constants;
import org.dungeon.util.Matches;
import org.dungeon.util.Messenger;
import org.dungeon.util.Percentage;
import org.dungeon.util.Utils;

public class Hero extends Creature {
	private static final long serialVersionUID = 1L;
	
	private static final int MILLISECONDS_TO_SLEEP_AN_HOUR = 500;
	private static final int SECONDS_TO_LOOK_AT_THE_COVER_OF_A_BOOK = 6;
	private static final int SECONDS_TO_PICK_UP_AN_ITEM = 10;
	private static final int SECONDS_TO_DESTROY_AN_ITEM = 120;
	private static final int SECONDS_TO_LEARN_A_SKILL = 60;
	private static final int SECONDS_TO_EAT_AN_ITEM = 30;
	private static final int SECONDS_TO_DROP_AN_ITEM = 2;
	private static final int SECONDS_TO_UNEQUIP = 4;
	private static final int SECONDS_TO_EQUIP = 6;
	private static final String ROTATION_SKILL_SEPARATOR = ">";
	private static final Percentage LUMINOSITY_TO_SEE_ADJACENT_LOCATIONS = new Percentage(0.4);
	
	private final AchievementTracker achievementTracker;
	private final Date dateOfBirth;
	
	public Hero() {
		super(Constants.HERO_ID, "Hero", Name.newInstance("Seth"), 50, 5, "HERO");
		setInventory(new CreatureInventory(this, 12, 10));
		dateOfBirth = new Date(432, 6, 4, 8, 30, 0);
		achievementTracker = new AchievementTracker();
	}
	
	public AchievementTracker getAchievementTracker() {
		return achievementTracker;
	}
	
	private boolean isCompletelyHealed() {
		return getMaxHealth() == getCurHealth();
	}
	
	public int rest() {
		final double healthFractionThroughRest = 0.6;
		if (getCurHealth() >= (int) (healthFractionThroughRest * getMaxHealth())) {
			IO.writeString("You are already rested.");
			return 0;
		} else {
			double fractionHealed = healthFractionThroughRest - (double) getCurHealth() / (double) getMaxHealth();
			IO.writeString("Resting...");
			setCurHealth((int) (healthFractionThroughRest * getMaxHealth()));
			IO.writeString("You feel rested.");
			return (int) (TimeConstants.HEAL_TEN_PERCENT * fractionHealed * 10);
		}
	}
	
	public int sleep() {
		int seconds = 0;
		World world = getLocation().getWorld();
		PartOfDay pod = world.getPartOfDay();
		if (pod == PartOfDay.EVENING || pod == PartOfDay.MIDNIGHT || pod == PartOfDay.NIGHT) {
			IO.writeString("You fall asleep.");
			seconds = PartOfDay.getSecondsToNext(world.getWorldDate(), PartOfDay.DAWN);
			seconds += Engine.RANDOM.nextInt(15 * 60 + 1);
			int healing = getMaxHealth() * seconds / TimeConstants.HEAL_TEN_PERCENT / 10;
			if (!isCompletelyHealed()) {
				int health = getCurHealth() + healing;
				if (health < getMaxHealth()) {
					setCurHealth(health);
				} else {
					setCurHealth(getMaxHealth());
				}
			}
			final int dreamDurationInSeconds = 4 * 60 * 60;
			int remainingSeconds = seconds;
			while (remainingSeconds > 0) {
				if (remainingSeconds > dreamDurationInSeconds) {
					Sleeper.sleep(MILLISECONDS_TO_SLEEP_AN_HOUR * dreamDurationInSeconds / 3600);
					IO.writeString(GameData.getDreamLibrary().getNextDream());
					remainingSeconds -= dreamDurationInSeconds;
				} else {
					Sleeper.sleep(MILLISECONDS_TO_SLEEP_AN_HOUR * remainingSeconds / 3600);
					break;
				}
			}
			IO.writeString("You wake up.");
		} else {
			IO.writeString("You can only sleep at night.");
		}
		return seconds;
	}
	
	private boolean canSee(Entity entity) {
		return getLocation().getLuminosity().biggerThanOrEqualTo(entity.getVisibility());
	}
	
	private boolean canSeeACreature() {
		for (Creature creature : getLocation().getCreatures()) {
			if (creature != this && canSee(creature)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean canSeeAnItem() {
		for (Item item : getLocation().getItemList()) {
			if (canSee(item)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean canSeeAdjacentLocations() {
		return getLocation().getLuminosity().biggerThanOrEqualTo(LUMINOSITY_TO_SEE_ADJACENT_LOCATIONS);
	}
	
	private <T extends Entity> List<T> filterByVisibility(List<T> list) {
		List<T> visible = new ArrayList<T>();
		for (T entity : list) {
			if (canSee(entity)) {
				visible.add(entity);
			}
		}
		return visible;
	}
	
	private <T extends Entity> Matches<T> filterByVisibility(Matches<T> matches) {
		return Matches.fromCollection(filterByVisibility(matches.toList()));
	}
	
	public void look(Direction walkedInFrom) {
		Location location = getLocation();
		String firstLine;
		if (walkedInFrom != null) {
			firstLine = "You arrive at " + location.getName() + ".";
		} else {
			firstLine = "You are at " + location.getName() + ".";
		}
		firstLine += " " + "It is " + location.getWorld().getPartOfDay().toString().toLowerCase() + ".";
		IO.writeString(firstLine);
		IO.writeNewLine();
		if (canSeeAdjacentLocations()) {
			lookAdjacentLocations(walkedInFrom);
		} else {
			IO.writeString("You can't clearly see the surrounding locations.");
		}
		IO.writeNewLine();
		lookCreatures();
		IO.writeNewLine();
		lookItems();
	}
	
	private void lookAdjacentLocations(Direction walkedInFrom) {
		World world = Game.getGameState().getWorld();
		Point pos = Game.getGameState().getHeroPosition();
		
		HashMap<String, ArrayList<Direction>> visibleLocations = new HashMap<String, ArrayList<Direction>>();
		
		for (Direction dir : Direction.values()) {
			if (walkedInFrom == null || !dir.equals(walkedInFrom)) {
				Point adjacentPoint = new Point(pos, dir);
				Location adjacentLocation = world.getLocation(adjacentPoint);
				ExplorationStatistics explorationStatistics = Game.getGameState().getStatistics().getExplorationStatistics();
				explorationStatistics.createEntryIfNotExists(adjacentPoint, adjacentLocation.getID());
				String locationName = adjacentLocation.getName();
				if (!visibleLocations.containsKey(locationName)) {
					visibleLocations.put(locationName, new ArrayList<Direction>());
				}
				visibleLocations.get(locationName).add(dir);
			}
		}
		
		StringBuilder stringBuilder = new StringBuilder(140);
		for (Entry<String, ArrayList<Direction>> entry : visibleLocations.entrySet()) {
			stringBuilder.append("To ");
			stringBuilder.append(Utils.enumerate(entry.getValue()));
			stringBuilder.append(" you see ");
			stringBuilder.append(entry.getKey());
			stringBuilder.append(".\n");
		}
		
		IO.writeString(stringBuilder.toString());
	}
	
	private void lookCreatures() {
		List<Creature> creatures = new ArrayList<Creature>(getLocation().getCreatures());
		creatures.remove(this);
		creatures = filterByVisibility(creatures);
		if (creatures.isEmpty()) {
			IO.writeString("You don't see anyone here.");
		} else {
			IO.writeString("Here you can see " + enumerateEntities(creatures) + ".");
		}
	}
	
	private void lookItems() {
		List<Item> items = getLocation().getItemList();
		items = filterByVisibility(items);
		if (!items.isEmpty()) {
			IO.writeString("On the ground you see " + enumerateEntities(getLocation().getItemList()) + ".");
		}
	}
	
	private int countEntitiesByName(final Collection<? extends Entity> entities, String name) {
		int counter = 0;
		for (Entity entity : entities) {
			if (entity.getName().equals(name)) {
				counter++;
			}
		}
		return counter;
	}
	
	private String enumerateEntities(final List<? extends Entity> listOfEntities) {
		ArrayList<String> quantifiedNames = new ArrayList<String>();
		ArrayList<String> alreadyListedEntities = new ArrayList<String>();
		for (Entity entity : listOfEntities) {
			String name = entity.getName();
			if (!alreadyListedEntities.contains(name)) {
				int count = countEntitiesByName(listOfEntities, name);
				quantifiedNames.add(entity.getQuantifiedName(count));
				alreadyListedEntities.add(name);
			}
		}
		return Utils.enumerate(quantifiedNames);
	}
	
	Item selectInventoryItem(IssuedCommand issuedCommand) {
		if (getInventory().getItemCount() == 0) {
			IO.writeString("Your inventory is empty.");
			return null;
		} else {
			return selectItem(issuedCommand, getInventory(), false);
		}
	}
	
	Item selectLocationItem(IssuedCommand issuedCommand) {
		if (filterByVisibility(getLocation().getItemList()).isEmpty()) {
			IO.writeString("You don't see any items here.");
			return null;
		} else {
			return selectItem(issuedCommand, getLocation().getInventory(), true);
		}
	}
	
	private Item selectItem(IssuedCommand issuedCommand, BaseInventory inventory, boolean checkForVisibility) {
		List<Item> visibleItems;
		if (checkForVisibility) {
			visibleItems = filterByVisibility(inventory.getItems());
		} else {
			visibleItems = inventory.getItems();
		}
		if (issuedCommand.hasArguments() || checkIfAllEntitiesHaveTheSameName(visibleItems)) {
			return findItem(visibleItems, issuedCommand.getArguments());
		} else {
			IO.writeString("You must specify an item.");
			return null;
		}
	}
	
	public Item findItem(List<Item> items, String[] tokens) {
		Matches<Item> matches = Utils.findBestCompleteMatches(items, tokens);
		matches = filterByVisibility(matches);
		if (matches.size() == 0) {
			IO.writeString("Item not found.");
		} else if (matches.size() == 1 || matches.getDifferentNames() == 1) {
			return matches.getMatch(0);
		} else {
			Messenger.printAmbiguousSelectionMessage();
		}
		return null;
	}
	
	public int attackTarget(IssuedCommand issuedCommand) {
		if (canSeeACreature()) {
			Creature target = selectTarget(issuedCommand);
			if (target != null) {
				return Engine.battle(this, target) * TimeConstants.BATTLE_TURN_DURATION;
			}
		} else {
			IO.writeString("You do not see a possible target.");
		}
		return 0;
	}
	
	Creature selectTarget(IssuedCommand issuedCommand) {
		List<Creature> visibleCreatures = filterByVisibility(getLocation().getCreatures());
		if (issuedCommand.hasArguments() || checkIfAllEntitiesHaveTheSameName(visibleCreatures, this)) {
			return findCreature(issuedCommand.getArguments());
		} else {
			IO.writeString("You must specify a target.");
			return null;
		}
	}
	
	Creature findCreature(String[] tokens) {
		Matches<Creature> result = Utils.findBestCompleteMatches(getLocation().getCreatures(), tokens);
		
		if (result.size() == 0) {
			IO.writeString("Creature not found.");
		} else if (result.size() == 1 || result.getDifferentNames() == 1) {
			return result.getMatch(0);
		} else if (result.getDifferentNames() == 2 && result.hasMatchWithName(getName())) {
			return result.getMatch(0).getName().equals(getName()) ? result.getMatch(1) : result.getMatch(0);
		} else {
			Messenger.printAmbiguousSelectionMessage();
		}
		
		return null;
	}

	private boolean checkIfAllEntitiesHaveTheSameName(Collection<? extends Entity> entities) {
		return checkIfAllEntitiesHaveTheSameName(entities, null);
	}
	
	private boolean checkIfAllEntitiesHaveTheSameName(Collection<? extends Entity> entities, Entity ignored) {
		String lastSeenName = null;
		for (Entity entity : entities) {
			if (ignored  == null || entity != ignored) {
				if (lastSeenName == null) {
					lastSeenName = entity.getName();
				} else {
					if (!entity.getName().equals(lastSeenName)) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	public int pickItem(IssuedCommand issuedCommand) {
		if (canSeeAnItem()) {
			Item selectedItem = selectLocationItem(issuedCommand);
			if (selectedItem != null) {
				if (getInventory().addItem(selectedItem)) {
					getLocation().removeItem(selectedItem);
					return SECONDS_TO_PICK_UP_AN_ITEM;
				}
			}
		} else {
			IO.writeString("You do not see ant item you could pick up.");
		}
		return 0;
	}
	
	public int parseEquip(IssuedCommand issuedCommand) {
		Item selectedItem = selectInventoryItem(issuedCommand);
		if (selectedItem != null) {
			if (selectedItem.hasTag(Item.Tag.WEAPON)) {
				equipWeapon(selectedItem);
				return SECONDS_TO_EQUIP;
			} else {
				IO.writeString("You cannot equip that.");
			}
		}
		return 0;
	}
	
	public int dropItem(IssuedCommand issuedCommand) {
		Item selectedItem = selectInventoryItem(issuedCommand);
		if (selectedItem != null) {
			int totalTime = SECONDS_TO_DROP_AN_ITEM;
			if (selectedItem == getWeapon()) {
				totalTime += unequipWeapon();
				
			}
			
			getInventory().removeItem(selectedItem);
			getLocation().addItem(selectedItem);
			IO.writeString("Dropped " + selectedItem.getName() + ".");
			return totalTime;
		}
		return 0;
	}
	
	public void printInventory() {
		Name item = Name.newInstance("item");
		String firstLine;
		if (getInventory().getItemCount() == 0) {
			firstLine = "Your inventory is empty.";
		} else {
			String itemCount = item.getQuantifiedName(getInventory().getItemCount(), QuantificationMode.NUMBER);
			firstLine = "You are carrying " + itemCount + ". Your inventory weights " + getInventory().getWeight() + ".";
		}
		IO.writeString(firstLine);
		String itemLimit = item.getQuantifiedName(getInventory().getItemLimit(), QuantificationMode.NUMBER);
		IO.writeString("Your maximum carrying capacity is " + itemLimit + " and " + getInventory().getWeightLimit() + ".");
		if (getInventory().getItemCount() != 0) {
			printItems();
		}
		if (hasWeapon()) {
			IO.writeString("You are equipping " + getWeapon().getQualifiedName() + ".");
		}
	}
	
	private void printItems() {
		ArrayList<String> names = new ArrayList<String>(getInventory().getItemCount());
		for (Item item : getInventory().getItems()) {
			names.add(String.format("%s (%s)", item.getQualifiedName(), item.getWeight()));
		}
		IO.writeString("You are carrying " + Utils.enumerate(names) + ".");
	}
	
	public int eatItem(IssuedCommand issuedCommand) {
		Item selectedItem = selectInventoryItem(issuedCommand);
		if (selectedItem != null) {
			if (selectedItem.hasTag(Item.Tag.FOOD)) {
				FoodComponent food = selectedItem.getFoodComponent();
				double remainingBites = selectedItem.getCurIntegrity() / (double) food.getIntegrityDecrementOnEat();
				if (remainingBites >= 1.0) {
					addHealth(food.getNutrition());
				} else {
					addHealth((int) (food.getNutrition() * remainingBites));
				}
				selectedItem.decrementIntegrity(food.getIntegrityDecrementOnEat());
				if (selectedItem.isBroken() && !selectedItem.hasTag(Item.Tag.REPAIRABLE)) {
					IO.writeString("You ate " + selectedItem.getName() + ".");
					getInventory().removeItem(selectedItem);
				} else {
					IO.writeString("You ate a bit of " + selectedItem.getName() + ".");
				}
				if (isCompletelyHealed()) {
					IO.writeString("You are completely healed.");
				}
				return SECONDS_TO_EAT_AN_ITEM;
			} else {
				IO.writeString("You can only eat food.");
			}
		}
		return 0;
	}
	
	public int readItem(IssuedCommand issuedCommand) {
		Item selectedItem = selectInventoryItem(issuedCommand);
		if (selectedItem != null) {
			BookComponent book = selectedItem.getBookComponent();
			if (book != null) {
				Skill skill = new Skill(GameData.getSkillDefinitions().get(book.getSkillID()));
				if (getSkillList().hasSkill(skill.getID())) {
					IO.writeString("You already know " + skill.getName() + ".");
					return SECONDS_TO_LOOK_AT_THE_COVER_OF_A_BOOK;
				} else {
					getSkillList().addSkill(skill);
					IO.writeString("You learned " + skill.getName() + ".");
					return SECONDS_TO_LEARN_A_SKILL;
				}
			} else {
				IO.writeString("You can only read books.");
			}
		}
		return 0;
	}
	
	public int destroyItem(IssuedCommand issuedCommand) {
		Item target = selectLocationItem(issuedCommand);
		
		if (target != null) {
			if (target.hasTag(Item.Tag.REPAIRABLE)) {
				if (target.isBroken()) {
					IO.writeString(target.getName() + " is already crashed.");
				} else {
					target.setCurIntegrity(0);
					IO.writeString(getName() + " crashed " + target.getName() + ".");
				}
			} else {
				getLocation().removeItem(target);
				IO.writeString(getName() + " destroyed " + target.getName() + ".");
			}
			return SECONDS_TO_DESTROY_AN_ITEM;
		}
		return 0;
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
	
	public int unequipWeapon() {
		if (hasWeapon()) {
			IO.writeString(getName() + " unequipped " + getWeapon().getName() + ".");
			setWeapon(null);
			return SECONDS_TO_UNEQUIP;
		} else {
			IO.writeString("You are not equipping a weapon.");
		}
		return 0;
	}
	
	public void printAllStatus() {
		StringBuilder builder = new StringBuilder();
		builder.append(getName()).append('\n');
		builder.append("You are ");
		builder.append(HealthState.getHealthState(getCurHealth(), getMaxHealth()).toString().toLowerCase()).append(".\n");
		builder.append("Your base attack is ").append(String.valueOf(getAttack())).append(".\n");
		if (hasWeapon()) {
			String format = "You are currently equipping %s, whose base damage is %d. This makes your total damage %d.\n";
			int weaponBaseDamage = getWeapon().getWeaponComponent().getDamage();
			int totalDamage = getAttack() + weaponBaseDamage;
			builder.append(String.format(format, getWeapon().getQualifiedName(), weaponBaseDamage, totalDamage));
		} else {
			builder.append("You are fighting bare-handed.\n");
		}
		IO.writeString(builder.toString());
	}
	
	public void printAge() {
		String age = new Period(dateOfBirth, Game.getGameState().getWorld().getWorldDate()).toString();
		IO.writeString(String.format("You are %s old.", age), Color.CYAN);
	}
	
	public int printDateAndTime() {
        Item clock = null;
        int timeSpent = 0;
        if (hasWeapon() && getWeapon().hasTag(Item.Tag.CLOCK)) {
        	if (!getWeapon().isBroken()) {
        		clock = getWeapon();
        		timeSpent = 4;
        	} else {
        		for (Item item : getInventory().getItems()) {
        			if (item.hasTag(Item.Tag.CLOCK) && !item.isBroken()) {
        				clock = item;
        				timeSpent = 10;
        				break;
        			}
        		}
        		if (clock == null) {
        			clock = getWeapon();
        			timeSpent = 4;
        		}
        	}
        } else {
        	Item brokenClock = null;
        	for (Item item : getInventory().getItems()) {
        		if (item.hasTag(Item.Tag.CLOCK)) {
        			if (item.isBroken() && brokenClock == null) {
        				brokenClock = item;
        			} else {
        				clock = item;
        				timeSpent = 10;
        				break;
        			}
        		}
        	}
        	if (brokenClock != null) {
        		timeSpent = 10;
        		clock = brokenClock;
        	}
        }
        if (clock != null) {
        	IO.writeString(clock.getClockComponent().getTimeString());
        }
        
        World world = getLocation().getWorld();
        Date worldDate = world.getWorldDate();
        IO.writeString("You think it is " + worldDate.toDateString() + ".");

        if (worldDate.getMonth() == dateOfBirth.getMonth() && worldDate.getDay() == dateOfBirth.getDay()) {
            IO.writeString("Today is your birthday.");
        }

        IO.writeString("You can see that it is " + world.getPartOfDay().toString().toLowerCase() + ".");
        return timeSpent;
    }
	
	public void printSkills() {
		if (getSkillList().getSize() == 0) {
			IO.writeString("You have not learned any skills yet.");
		} else {
			IO.writeString("You know the following skills:");
			getSkillList().printSkillList();
		}
	}
	
	public void editRotation(IssuedCommand issuedCommand) {
		if (issuedCommand.hasArguments()) {
			List<String[]> skillNames = new ArrayList<String[]>();
			List<String> currentSkillName = new ArrayList<String>();
			for (String argument : issuedCommand.getArguments()) {
				if (ROTATION_SKILL_SEPARATOR.equals(argument)) {
					if (!currentSkillName.isEmpty()) {
						String[] stringArray = new String[currentSkillName.size()];
						currentSkillName.toArray(stringArray);
						skillNames.add(stringArray);
						currentSkillName.clear();
					}
				} else {
					currentSkillName.add(argument);
				}
			}
			if (!currentSkillName.isEmpty()) {
				String[] stringArray = new String[currentSkillName.size()];
				currentSkillName.toArray(stringArray);
				skillNames.add(stringArray);
				currentSkillName.clear();
			}
			if (skillNames.isEmpty()) {
				IO.writeString("Provide skills arguments separated by '" + ROTATION_SKILL_SEPARATOR + ".");
			} else {
				getSkillRotation().resetRotation();
				ArrayList<Selectable> skillsList = new ArrayList<Selectable>(getSkillList().toListOfSelectable());
				for (String[] skillName : skillNames) {
					Matches<Selectable> result = Utils.findBestCompleteMatches(skillsList, skillName);
					if (result.size() == 0) {
						IO.writeString(Utils.stringArrayToString(skillName, " ") + " did not match any skill!");
					} else {
						if (result.getDifferentNames() == 1) {
							getSkillRotation().addSkill((Skill) result.getMatch(0));
						} else {
							IO.writeString(Utils.stringArrayToString(skillName, " ") + " matched multiple skills!");
						}
					}
				}
				if (getSkillRotation().isEmpty()) {
					IO.writeString("Failed to create a new skill rotation.");
				} else {
					IO.writeString("Created new skill rotation.");
				}
			}
		} else {
			if (getSkillRotation().isEmpty()) {
				IO.writeString("You don't have a skill rotation.");
			} else {
				IO.writeString("This is your current skill rotation:");
				getSkillRotation().printSkillRotation();
			}
		}
	}
	
	public int castRepairOnEquippedItem() {
		ID repairID = new ID("REPAIR");
		if (getSkillList().hasSkill(repairID)) {
			if (hasWeapon()) {
				if (getWeapon().hasTag(Item.Tag.REPAIRABLE)) {
					getWeapon().incrementIntegrity(GameData.getSkillDefinitions().get(repairID).repair);
					IO.writeString("You casted Repair on " + getWeapon().getName() + ".");
					return 10;
				} else {
					IO.writeString("The equipped item is not repairable.");
				}
			} else {
				IO.writeString("You are not equipping anything.");
			}
		} else {
			IO.writeString("You don't know how to cast Repair.");
		}
		return 0;
	}
	
}
