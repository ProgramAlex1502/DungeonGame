package main.java.org.dungeon.creatures;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import main.java.org.dungeon.achievements.AchievementTracker;
import main.java.org.dungeon.date.Date;
import main.java.org.dungeon.date.Period;
import main.java.org.dungeon.game.Direction;
import main.java.org.dungeon.game.Engine;
import main.java.org.dungeon.game.Game;
import main.java.org.dungeon.game.GameData;
import main.java.org.dungeon.game.IssuedCommand;
import main.java.org.dungeon.game.Location;
import main.java.org.dungeon.game.PartOfDay;
import main.java.org.dungeon.game.Point;
import main.java.org.dungeon.game.Selectable;
import main.java.org.dungeon.game.TimeConstants;
import main.java.org.dungeon.game.World;
import main.java.org.dungeon.io.IO;
import main.java.org.dungeon.io.Sleeper;
import main.java.org.dungeon.items.BookComponent;
import main.java.org.dungeon.items.CreatureInventory;
import main.java.org.dungeon.items.FoodComponent;
import main.java.org.dungeon.items.Item;
import main.java.org.dungeon.skill.Skill;
import main.java.org.dungeon.util.Constants;
import main.java.org.dungeon.util.SelectionResult;
import main.java.org.dungeon.util.Utils;

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
	
	private final Date dateOfBirth;
	private final AchievementTracker achievementTracker;

	public Hero(String name) {
		super(makeHeroBlueprint(name));
		setInventory(new CreatureInventory(this, 3));
		dateOfBirth = new Date(432, 6, 4, 8, 30, 0);
		achievementTracker = new AchievementTracker();
	}
	
	private static CreatureBlueprint makeHeroBlueprint(String name) {
		CreatureBlueprint heroBlueprint = new CreatureBlueprint();
		heroBlueprint.setID(Constants.HERO_ID);
		heroBlueprint.setName(name);
		heroBlueprint.setType("Hero");
		heroBlueprint.setAttack(4);
		heroBlueprint.setAttackAlgorithmID("HERO");
		heroBlueprint.setMaxHealth(50);
		heroBlueprint.setCurHealth(50);
		return heroBlueprint;
	}
	
	public AchievementTracker getAchievementTracker() {
		return achievementTracker;
	}
	
	private Date getDateOfBirth() {
		return dateOfBirth;
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
			while (seconds > 0) {
				System.out.println(GameData.getDreamLibrary().getNextDream());
				if (seconds > dreamDurationInSeconds) {
					Sleeper.sleep(MILLISECONDS_TO_SLEEP_AN_HOUR * dreamDurationInSeconds / 3600);
					
					IO.writeString(GameData.getDreamLibrary().getNextDream());
					seconds -= dreamDurationInSeconds;
				} else {
					Sleeper.sleep(MILLISECONDS_TO_SLEEP_AN_HOUR * seconds / 3600);
					break;
				}
			}
			IO.writeString("You wake up.");
		} else {
			IO.writeString("You can only sleep at night.");
		}
		
		return seconds;
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
		
		if (canSee()) {
			lookAdjacentLocations(walkedInFrom);
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
						curCount = location.getCreatureCount(creature.getID());
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
	
	private void lookAdjacentLocations(Direction walkedInFrom) {
		World world = Game.getGameState().getWorld();
		Point pos = Game.getGameState().getHeroPosition();
		
		HashMap<String, ArrayList<Direction>> visibleLocations = new HashMap<String, ArrayList<Direction>>();
		
		for(Direction dir : Direction.values()) {
			if (walkedInFrom == null || !dir.equals(walkedInFrom)) {
				String locationName = world.getLocation(new Point(pos, dir)).getName();
				if (!visibleLocations.containsKey(locationName)) {
					visibleLocations.put(locationName, new ArrayList<Direction>());
				}
				visibleLocations.get(locationName).add(dir);
			}
		}
		
		StringBuilder stringBuilder = new StringBuilder(140);
		for (Entry<String, ArrayList<Direction>> entry : visibleLocations.entrySet()) {
			stringBuilder.append("To ");
			stringBuilder.append(enumerateDirections(entry.getValue()));
			stringBuilder.append(" you see ");
			stringBuilder.append(entry.getKey());
			stringBuilder.append(".\n");			
		}
		
		IO.writeString(stringBuilder.toString());
		IO.writeNewLine();
	}
	
	private String enumerateDirections(List<Direction> directions) {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 0; i < directions.size(); i++) {
			stringBuilder.append(directions.get(i).toString().toLowerCase());
			if (i < directions.size() - 2) {
				stringBuilder.append(", ");
			} else if (i == directions.size() - 2) {
				stringBuilder.append(" and ");
			}
		}
		
		return stringBuilder.toString();
	}
	
	boolean canSee() {
		double minimumLuminosity = 0.3;
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
	
	Creature selectTarget(IssuedCommand issuedCommand) {
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
	
	Creature findCreature(String[] tokens) {
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
	
	public int pickItem(IssuedCommand issuedCommand) {
		if (canSee()) {
			Item selectedItem = selectLocationItem(issuedCommand);
			if (selectedItem != null) {
				if (getInventory().isFull()) {
					IO.writeString(Constants.INVENTORY_FULL);
				} else {
					getInventory().addItem(selectedItem);
					getLocation().removeItem(selectedItem);
					return SECONDS_TO_PICK_UP_AN_ITEM;
				}
			}
		} else {
			IO.writeString("It is too dark for you too see anything.");
		}
		return 0;
	}
	
	public int parseEquip(IssuedCommand issuedCommand) {
		Item selectedItem = selectInventoryItem(issuedCommand);
		
		if (selectedItem != null) {
			if (selectedItem.isWeapon()) {
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
		IO.writeString("Items: " + getInventory().getItemCount() + "/" + getInventory().getLimit(), Color.CYAN);
		getInventory().printItems();
	}
	
	public int eatItem(IssuedCommand issuedCommand) {
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
			return SECONDS_TO_DESTROY_AN_ITEM;
		}
		return 0;
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
	
	public int unequipWeapon() {
		if (hasWeapon()) {
			IO.writeString(getName() + " unequipped " + getWeapon().getName() + ".");
			setWeapon(null);
			return SECONDS_TO_UNEQUIP;
		} else {
			IO.writeString(Constants.NOT_EQUIPPING_A_WEAPON);
		}
		return 0;
	}
	
	public void printAllStatus() {
		StringBuilder builder = new StringBuilder();
		builder.append(getName()).append("\n");
		builder.append("You are ");
		builder.append(HealthState.getHealthState(getCurHealth(), getMaxHealth()).toString().toLowerCase()).append(".\n");
		builder.append("Your base attack is ").append(String.valueOf(getAttack())).append(".\n");
		if (hasWeapon()) {
			Item heroWeapon = getWeapon();
			builder.append("You are currently equipping ").append(heroWeapon.getQualifiedName());
			builder.append(", whose base damage is ").append(String.valueOf(heroWeapon.getDamage())).append(".\n");
			builder.append("This makes your total damage ").append(getAttack() + heroWeapon.getDamage()).append(".\n");
		} else {
			builder.append("You are fighting bare-handed.\n");
		}
		IO.writeString(builder.toString());
	}
	
	public void printAge() {
		String age = new Period(getDateOfBirth(), Game.getGameState().getWorld().getWorldDate()).toString();
		IO.writeString(String.format("You are %s old.", age), Color.CYAN);
	}
	
	public int printDateAndTime() {
		World world = getLocation().getWorld();
		int timeSpent = 2;
		if (hasClock()) {
			if (hasWeapon() && getWeapon().isClock() && !getWeapon().isBroken()) {
				timeSpent += 2;
			} else {
				timeSpent += 8;
			}
			
			IO.writeString(getClock().getClockComponent().getTimeString());
		}
		
		Date worldDate = world.getWorldDate();
		IO.writeString("You think it is " + worldDate.toDateString() + ".");
		
		Date dob = getDateOfBirth();
		if (worldDate.getMonth() == dob.getMonth() && worldDate.getDay() == dob.getDay()) {
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
				IO.writeString("Provide skills arguments separated by '" + ROTATION_SKILL_SEPARATOR + "'.");
			} else {
				getSkillRotation().resetRotation();
				ArrayList<Selectable> skillsList = new ArrayList<Selectable>(getSkillList().toListOfSelectable());
				for (String[] skillName : skillNames) {
					SelectionResult<Selectable> result = Utils.selectFromList(skillsList, skillName);
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
					IO.writeString("Created a new skill rotation.");
				}
			}
		} else {
			if (getSkillRotation().isEmpty()) {
				IO.writeString("You don't have a skill rotation.");
			} else {
				IO.writeString("This is your current skill rotation.");
				getSkillRotation().printSkillRotation();
			}
		}
	}

}
