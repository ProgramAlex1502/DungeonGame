package org.dungeon.game;

import static org.dungeon.date.DungeonTimeUnit.DAY;
import static org.dungeon.date.DungeonTimeUnit.SECOND;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dungeon.achievements.Achievement;
import org.dungeon.achievements.AchievementBuilder;
import org.dungeon.achievements.BattleStatisticsQuery;
import org.dungeon.achievements.BattleStatisticsRequirement;
import org.dungeon.entity.Weight;
import org.dungeon.entity.creatures.AttackAlgorithmID;
import org.dungeon.entity.creatures.Creature;
import org.dungeon.entity.creatures.CreatureFactory;
import org.dungeon.entity.creatures.CreaturePreset;
import org.dungeon.entity.items.Item;
import org.dungeon.entity.items.ItemBlueprint;
import org.dungeon.entity.items.ItemFactory;
import org.dungeon.io.DLogger;
import org.dungeon.io.JsonObjectFactory;
import org.dungeon.io.ResourceReader;
import org.dungeon.skill.SkillDefinition;
import org.dungeon.stats.CauseOfDeath;
import org.dungeon.stats.TypeOfCauseOfDeath;
import org.dungeon.util.CounterMap;
import org.dungeon.util.StopWatch;

import com.eclipsesource.json.JsonObject;
import com.eclipsesource.json.JsonObject.Member;
import com.eclipsesource.json.JsonValue;


public final class GameData {

    private static final int CORPSE_DAMAGE = 2;
    private static final int CORPSE_INTEGRITY_DECREMENT_ON_HIT = 5;
    private static final long CORPSE_PUTREFACTION_PERIOD = DAY.as(SECOND);
    private static final double CORPSE_HIT_RATE = 0.5;
    private static final PoetryLibrary poetryLibrary = new PoetryLibrary();
    private static final DreamLibrary dreamLibrary = new DreamLibrary();
    private static final HintLibrary hintLibrary = new HintLibrary();
    private static final LocationPresetStore locationPresetStore = new LocationPresetStore();
    
    private static String tutorial = null;
    
    public static HashMap<ID, Achievement> ACHIEVEMENTS;
    
    public static String LICENSE;
    private static Map<ID, ItemBlueprint> itemBlueprints = new HashMap<ID, ItemBlueprint>();
    private static Map<ID, SkillDefinition> skillDefinitions = new HashMap<ID, SkillDefinition>();
    
    private GameData() {
    	throw new AssertionError();
    }
    
    public static PoetryLibrary getPoetryLibrary() {
    	return poetryLibrary;
    }
    
    public static DreamLibrary getDreamLibrary() {
    	return dreamLibrary;
    }
    
    public static HintLibrary getHintLibrary() {
    	return hintLibrary;
    }
    
    public static String getTutorial() {
    	if (tutorial == null) {
    		loadTutorial();
    	}
    	return tutorial;
    }

    static void loadGameData() {
    	StopWatch stopWatch = new StopWatch();
    	DLogger.info("Started loading the game data.");

        loadItemBlueprints();
        loadCreaturePresets();
        createSkills();
        loadLocationPresets();
        loadAchievements();
        loadLicense();

        DLogger.info("Finished loading the game data. Took " + stopWatch.toString() + ".");
    }
    
    
    private static void createSkills() {
    	if (!skillDefinitions.isEmpty()) {
    		throw new AssertionError();
    	}
    	SkillDefinition fireball = new SkillDefinition("FIREBALL", "Fireball", 10, 0, 6);
    	skillDefinitions.put(fireball.id, fireball);
    	
    	SkillDefinition burningGround = new SkillDefinition("BURNING_GROUND", "Burning Ground", 18, 0, 12);
    	skillDefinitions.put(burningGround.id, burningGround);
    	
    	SkillDefinition repair = new SkillDefinition("REPAIR", "Repair", 0, 40, 10);
    	skillDefinitions.put(repair.id, repair);
    	skillDefinitions = Collections.unmodifiableMap(skillDefinitions);
    }

    private static void loadItemBlueprints() {
		ResourceReader reader = new ResourceReader("items.txt");
        
        while (reader.readNextElement()) {
        	ItemBlueprint blueprint = new ItemBlueprint();
        	blueprint.setID(new ID(reader.getValue("ID")));
            blueprint.setType(reader.getValue("TYPE"));
            blueprint.setName(nameFromArray(reader.getArrayOfValues("NAME")));
            for (Item.Tag tag : tagSetFromArray(Item.Tag.class, reader.getArrayOfValues("TAGS"))) {
            	blueprint.addTag(tag);
            }
            if (blueprint.hasTag(Item.Tag.BOOK)) {
            	blueprint.setText(reader.getValue("TEXT"));
            }
            if (reader.hasValue("DECOMPOSITION_PERIOD")) {
            	blueprint.setPutrefactionPeriod(readIntegerFromResourceReader(reader, "DECOMPOSITION_PERIOD"));
            }
            blueprint.setCurIntegrity(readIntegerFromResourceReader(reader, "CUR_INTEGRITY"));
            blueprint.setMaxIntegrity(readIntegerFromResourceReader(reader, "MAX_INTEGRITY"));
            blueprint.setVisibility(reader.readVisibility());
            blueprint.setWeight(Weight.newInstance(readDoubleFromResourceReader(reader, "WEIGHT")));
            blueprint.setDamage(readIntegerFromResourceReader(reader, "DAMAGE"));
            blueprint.setHitRate(readDoubleFromResourceReader(reader, "HIT_RATE"));
            blueprint.setIntegrityDecrementOnHit(readIntegerFromResourceReader(reader, "INTEGRITY_DECREMENT_ON_HIT"));
            if (reader.hasValue("NUTRITION")) {
            	blueprint.setNutrition(readIntegerFromResourceReader(reader, "NUTRITION"));
            }
            if (reader.hasValue("INTEGRITY_DECREMENT_ON_EAT")) {
            	blueprint.setIntegrityDecrementOnEat(readIntegerFromResourceReader(reader, "INTEGRITY_DECREMENT_ON_EAT"));
            }
            if (reader.hasValue("SKILL")) {
            	blueprint.setSkill(reader.getValue("SKILL"));
            }
            itemBlueprints.put(blueprint.getID(), blueprint);
        }
        
        reader.close();
        DLogger.info("Loaded " + itemBlueprints.size() + " item blueprints.");
    }

    private static void loadCreaturePresets() {
    	Map<ID, CreaturePreset> creaturePresetMap = new HashMap<ID, CreaturePreset>();
		ResourceReader reader = new ResourceReader("creatures.txt");
        
        while (reader.readNextElement()) {
        	CreaturePreset preset = new CreaturePreset();
        	preset.setID(new ID(reader.getValue("ID")));
        	preset.setType(reader.getValue("TYPE"));
        	preset.setName(nameFromArray(reader.getArrayOfValues("NAME")));
        	if (reader.hasValue("TAGS")) {
        		for (Creature.Tag tag : tagSetFromArray(Creature.Tag.class, reader.getArrayOfValues("TAGS"))) {
        			preset.addTag(tag);
        		}
        	}
        	preset.setInventoryItemLimit(readIntegerFromResourceReader(reader, "INVENTORY_ITEM_LIMIT"));
        	preset.setInventoryWeightLimit(readDoubleFromResourceReader(reader, "INVENTORY_WEIGHT_LIMIT"));
        	preset.setVisibility(reader.readVisibility());
        	preset.setWeight(Weight.newInstance(readDoubleFromResourceReader(reader, "WEIGHT")));
        	preset.setHealth(readIntegerFromResourceReader(reader, "HEALTH"));
        	preset.setAttack(readIntegerFromResourceReader(reader, "ATTACK"));
        	preset.setAttackAlgorithmID(AttackAlgorithmID.valueOf(reader.getValue("ATTACK_ALGORITHM_ID")));
        	if (reader.hasValue("ITEMS")) {
        		preset.setItems(readIDList(reader, "ITEMS"));
        	}
        	if (reader.hasValue("WEAPON")) {
        		preset.setWeaponID(new ID(reader.getValue("WEAPON")));
        	}
        	creaturePresetMap.put(preset.getID(), preset);
        	
        	if (preset.hasTag(Creature.Tag.CORPSE)) {
        		ItemBlueprint corpse = makeCorpseBlueprint(preset);        		
        		itemBlueprints.put(corpse.getID(), corpse);
        	}
        }
        
        reader.close();
        itemBlueprints = Collections.unmodifiableMap(itemBlueprints);
        CreatureFactory.setCreaturePresetMap(Collections.unmodifiableMap(creaturePresetMap));
        DLogger.info("Loaded " + creaturePresetMap.size() + " creature blueprints.");
    }
    
    public static ItemBlueprint makeCorpseBlueprint(CreaturePreset preset) {
    	ItemBlueprint corpse = new ItemBlueprint();
    	corpse.setID(ItemFactory.makeCorpseIDFromCreatureID(preset.getID()));
    	corpse.setType("CORPSE");
    	corpse.setName(Name.newCorpseName(preset.getName()));
    	corpse.setWeight(preset.getWeight());
    	corpse.setPutrefactionPeriod(CORPSE_PUTREFACTION_PERIOD);
    	int integrity = (int) Math.ceil(preset.getHealth() / (double) 2);
    	corpse.setMaxIntegrity(integrity);
    	corpse.setCurIntegrity(integrity);
    	corpse.setVisibility(preset.getVisibility());
    	corpse.setHitRate(CORPSE_HIT_RATE);
    	corpse.setIntegrityDecrementOnHit(CORPSE_INTEGRITY_DECREMENT_ON_HIT);
    	corpse.setDamage(CORPSE_DAMAGE);
    	corpse.addTag(Item.Tag.WEAPON);
    	corpse.addTag(Item.Tag.WEIGHT_PROPORTIONAL_TO_INTEGRITY);
    	corpse.addTag(Item.Tag.DECOMPOSES);
    	return corpse;
    }

    private static void loadLocationPresets() {
        ResourceReader reader = new ResourceReader("locations.txt");
        while (reader.readNextElement()) {
        	ID id = new ID(reader.getValue("ID"));
        	LocationPreset.Type type = LocationPreset.Type.valueOf(reader.getValue("TYPE"));
        	Name name = nameFromArray(reader.getArrayOfValues("NAME"));
        	LocationPreset preset = new LocationPreset(id, type, name);
        	preset.setDescription(new LocationDescription(reader.readCharacter("SYMBOL"), reader.readColor()));
        	if (reader.hasValue("INFO")) {
        		preset.getDescription().setInfo(reader.getValue("INFO"));
        	}
        	preset.setBlobSize(readIntegerFromResourceReader(reader, "BLOB_SIZE"));
        	preset.setLightPermittivity(readDoubleFromResourceReader(reader, "LIGHT_PERMITTIVITY"));
        	if (reader.hasValue("SPAWNERS")) {
        		for (String dungeonList : reader.getArrayOfValues("SPAWNERS")) {
        			String[] spawner = ResourceReader.toArray(dungeonList);
        			String spawnerID = spawner[0];
        			int population = Integer.parseInt(spawner[1]);
        			int delay = Integer.parseInt(spawner[2]);
        			preset.addSpawner(new SpawnerPreset(spawnerID, population, delay));
        		}
        	}
        	if (reader.hasValue("ITEMS")) {
        		for (String dungeonList : reader.getArrayOfValues("ITEMS")) {
        			String[] item = ResourceReader.toArray(dungeonList);
        			String itemID = item[0];
        			double frequency = Double.parseDouble(item[1]);
        			preset.addItem(itemID, frequency);
        		}
        	}
        	if (reader.hasValue("BLOCKED_ENTRANCES")) {
        		for (String dungeonList : reader.getArrayOfValues("BLOCKED_ENTRANCES")) {
        			String[] entrances = ResourceReader.toArray(dungeonList);
        			for (String entrance : entrances) {
        				preset.block(Direction.fromAbbreviation(entrance));
        			}
        		}
        	}
        	locationPresetStore.addLocationPreset(preset);
        }
        reader.close();
        
        DLogger.info("Loaded " + locationPresetStore.getSize() + " location presets.");
    }

    private static void loadAchievements() {
        ACHIEVEMENTS = new HashMap<ID, Achievement>();
        
		JsonObject jsonObject = JsonObjectFactory.makeJsonObject("achievements.json");
		for (JsonValue achievementValue : jsonObject.get("achievements").asArray()) {
			JsonObject achievementObject = achievementValue.asObject();
			AchievementBuilder builder = new AchievementBuilder();
			builder.setID(achievementObject.get("id").asString());
			builder.setName(achievementObject.get("name").asString());
			builder.setInfo(achievementObject.get("info").asString());
			builder.setText(achievementObject.get("text").asString());
			JsonValue battleRequirements = achievementObject.get("battleRequirements");
			if (battleRequirements != null) {
				for (JsonValue requirementValue : battleRequirements.asArray()) {
					JsonObject requirementObject = requirementValue.asObject();
					JsonObject queryObject = requirementObject.get("query").asObject();
					BattleStatisticsQuery query = new BattleStatisticsQuery();
					JsonValue idValue = queryObject.get("id");
					if (idValue != null) {
						query.setID(new ID(idValue.asString()));
					}
					JsonValue typeValue = queryObject.get("type");
					if (typeValue != null) {
						query.setType(typeValue.asString());
					}
					JsonValue causeOfDeathValue = queryObject.get("causeOfDeath");
					if (causeOfDeathValue != null) {
						JsonObject causeOfDeathObject = causeOfDeathValue.asObject();
						TypeOfCauseOfDeath type = TypeOfCauseOfDeath.valueOf(causeOfDeathObject.get("type").asString());
						ID id = new ID(causeOfDeathObject.get("id").asString());
						query.setCauseOfDeath(new CauseOfDeath(type, id));
					}
					JsonValue partOfDayValue = queryObject.get("partOfDay");
					if (partOfDayValue != null) {
						query.setPartOfDay(PartOfDay.valueOf(partOfDayValue.asString()));
					}
					int count = requirementObject.get("count").asInt();
					BattleStatisticsRequirement requirement = new BattleStatisticsRequirement(query, count);
					builder.addBattleStatisticsRequirement(requirement);
				}
			}
			JsonValue explorationRequirements = achievementObject.get("explorationRequirements");
			if (explorationRequirements != null) {
				JsonValue killsByLocationID = explorationRequirements.asObject().get("killsByLocationID");
				if (killsByLocationID != null) {
					builder.setKillsByLocationID(IDCounterMapFromJsonObject(killsByLocationID.asObject()));
				}
				JsonValue maximumNumberOfVisits = explorationRequirements.asObject().get("maximumNumberOfVisits");
				if (maximumNumberOfVisits != null) {
					builder.setMaximumNumberOfVisits(IDCounterMapFromJsonObject(maximumNumberOfVisits.asObject()));
				}
				JsonValue visitedLocations = explorationRequirements.asObject().get("visitedLocations");
				if (visitedLocations != null) {
					builder.setVisitedLocations(IDCounterMapFromJsonObject(visitedLocations.asObject()));
				}
			}
			
			Achievement achievement = builder.createAchievement();
			ACHIEVEMENTS.put(achievement.getID(), achievement);
		}

        DLogger.info("Created " + ACHIEVEMENTS.size() + " achievements.");
    }
    
    private static CounterMap<ID> IDCounterMapFromJsonObject(JsonObject jsonObject) {
    	CounterMap<ID> counterMap = new CounterMap<ID>();
    	for (Member member : jsonObject) {
    		counterMap.incrementCounter(new ID(member.getName()), member.getValue().asInt());
    	}
    	return counterMap;
    }
    
    private static double readDoubleFromResourceReader(ResourceReader reader, String key) {
    	if (reader.hasValue(key)) {
    		try {
    			return Double.parseDouble(reader.getValue(key));
    		} catch (NumberFormatException log) {
    			DLogger.warning("Could not parse the value of " + key + ".");
    		}
    	}
    	return 0.0;
    }
    
    private static int readIntegerFromResourceReader(ResourceReader reader, String key) {
    	if (reader.hasValue(key)) {
    		try {
    			return Integer.parseInt(reader.getValue(key));
    		} catch (NumberFormatException log) {
    			DLogger.warning("Could not parse the value of " + key + ".");
    		}
    	}
    	return 0;
    }
    
    private static Name nameFromArray(String[] strings) {
    	if (strings.length == 1) {
    		return Name.newInstance(strings[0]);
    	} else if (strings.length > 1) {
    		return Name.newInstance(strings[0], strings[1]);
    	} else {
    		DLogger.warning("Empty array used to create a Name! Using \"ERROR\".");
    		return Name.newInstance("ERROR");
    	}
    }
    
    private static <E extends Enum<E>> Set<E> tagSetFromArray(Class<E> enumClass, String[] strings) {
    	Set<E> set = EnumSet.noneOf(enumClass);
    	if (strings.length > 0) {
    		for (String tag : strings) {
    			try {
    				set.add(Enum.valueOf(enumClass, tag));
    			} catch (IllegalArgumentException fatal) {
    				String message = "Invalid tag '" + tag + "' found!";
    				throw new InvalidTagException(message, fatal);
    			}
    		}
    	} else {
    		DLogger.warning("Empty array used to create a Tag Set! Using empty set.");
    	}
    	return set;
    }
    
    private static List<ID> readIDList(ResourceReader reader, String key) {
    	List<ID> list = new ArrayList<ID>();
    	for (String id : reader.getArrayOfValues(key)) {
    		list.add(new ID(id));
    	}
    	return list;
    }

    private static void loadLicense() {
        ResourceReader reader = new ResourceReader("license.txt");
        reader.readNextElement();
        LICENSE = reader.getValue("LICENSE");
        reader.close();
    }
    
    private static void loadTutorial() {
    	if (tutorial != null) {
    		throw new AssertionError();
    	}
    	tutorial = JsonObjectFactory.makeJsonObject("tutorial.json").get("tutorial").asString();
    }
    
    public static Map<ID, ItemBlueprint> getItemBlueprints() {
    	return itemBlueprints;
    }
    
    public static Map<ID, SkillDefinition> getSkillDefinitions() {
    	return skillDefinitions;
    }
    
    public static LocationPresetStore getLocationPresetStore() {
    	return locationPresetStore;
    }
    
    public static class InvalidTagException extends IllegalArgumentException {
		private static final long serialVersionUID = 1L;

		public InvalidTagException(String message, Throwable cause) {
    		super(message, cause);
    	}
    }

}
