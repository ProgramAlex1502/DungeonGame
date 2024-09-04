package org.dungeon.game;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.dungeon.achievements.Achievement;
import org.dungeon.achievements.AchievementBuilder;
import org.dungeon.creatures.Creature;
import org.dungeon.io.DLogger;
import org.dungeon.io.ResourceReader;
import org.dungeon.items.Item;
import org.dungeon.items.ItemBlueprint;
import org.dungeon.skill.SkillDefinition;
import org.dungeon.stats.CauseOfDeath;
import org.dungeon.stats.TypeOfCauseOfDeath;
import org.dungeon.util.CounterMap;
import org.dungeon.util.StopWatch;

public final class GameData {

    public static final Font FONT = getMonospacedFont();
    private static final PoetryLibrary poetryLibrary = new PoetryLibrary();
    private static final DreamLibrary dreamLibrary = new DreamLibrary();
    private static final HintLibrary hintLibrary = new HintLibrary();
    
    private static String tutorial = null;
    
    public static HashMap<ID, Achievement> ACHIEVEMENTS;
    
    public static String LICENSE;
    private static Map<ID, Creature> creatures = new HashMap<ID, Creature>(20, 1f);
    private static Map<ID, ItemBlueprint> itemBlueprints = new HashMap<ID, ItemBlueprint>(20, 1f);
    private static Map<ID, SkillDefinition> skillDefinitions = new HashMap<ID, SkillDefinition>(2, 1f);
    private static Map<ID, LocationPreset> locationPresets = new HashMap<ID, LocationPreset>();
    
    private GameData() {
    	throw new AssertionError();
    }

    private static Font getMonospacedFont() {
    	final int FONT_SIZE = 15;
    	Font font = new Font(Font.MONOSPACED, Font.PLAIN, FONT_SIZE);
    	
    	try {
    		InputStream fontStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("DroidSansMono.ttf");
    		font = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(Font.PLAIN, FONT_SIZE);
    	} catch (FontFormatException bad) {
    		DLogger.warning(bad.getMessage());
    	} catch (IOException bad) {
    		DLogger.warning(bad.getMessage());
    	}
    	return font;
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
        loadCreatureBlueprints();
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
            blueprint.setTags(itemTagSetFromArray(reader.getArrayOfValues("TAGS")));
            blueprint.setCurIntegrity(readIntegerFromResourceReader(reader, "CUR_INTEGRITY"));
            blueprint.setMaxIntegrity(readIntegerFromResourceReader(reader, "MAX_INTEGRITY"));
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
        itemBlueprints = Collections.unmodifiableMap(itemBlueprints);
        DLogger.info("Loaded " + itemBlueprints.size() + " item blueprints.");
    }

    private static void loadCreatureBlueprints() {
		ResourceReader resourceReader = new ResourceReader("creatures.txt");
        
        while (resourceReader.readNextElement()) {
        	ID id = new ID(resourceReader.getValue("ID"));
        	String type = resourceReader.getValue("TYPE");
        	Name name = nameFromArray(resourceReader.getArrayOfValues("NAME"));
        	int health = readIntegerFromResourceReader(resourceReader, "HEALTH");
        	int attack = readIntegerFromResourceReader(resourceReader, "ATTACK");
        	String attackAlgorithmID = resourceReader.getValue("ATTACK_ALGORITHM_ID");
        	creatures.put(id, new Creature(id, type, name, health, attack, attackAlgorithmID));
        }
        
        resourceReader.close();
        creatures = Collections.unmodifiableMap(creatures);
        DLogger.info("Loaded " + creatures.size() + " creature blueprints.");
    }

    private static void loadLocationPresets() {
        ResourceReader resourceReader = new ResourceReader("locations.txt");
        while (resourceReader.readNextElement()) {
        	ID id = new ID(resourceReader.getValue("ID"));
        	String type = resourceReader.getValue("TYPE");
        	Name name = nameFromArray(resourceReader.getArrayOfValues("NAME"));
        	LocationPreset preset = new LocationPreset(id, type, name);
        	preset.setBlobSize(readIntegerFromResourceReader(resourceReader, "BLOB_SIZE"));
        	preset.setLightPermittivity(readDoubleFromResourceReader(resourceReader, "LIGHT_PERMITTIVITY"));
        	if (resourceReader.hasValue("SPAWNERS")) {
        		for (String dungeonList : resourceReader.getArrayOfValues("SPAWNERS")) {
        			String[] spawner = ResourceReader.toArray(dungeonList);
        			String spawnerID = spawner[0];
        			int population = Integer.parseInt(spawner[1]);
        			int delay = Integer.parseInt(spawner[2]);
        			preset.addSpawner(new SpawnerPreset(spawnerID, population, delay));
        		}
        	}
        	if (resourceReader.hasValue("ITEMS")) {
        		for (String dungeonList : resourceReader.getArrayOfValues("ITEMS")) {
        			String[] item = ResourceReader.toArray(dungeonList);
        			String itemID = item[0];
        			double frequency = Double.parseDouble(item[1]);
        			preset.addItem(itemID, frequency);
        		}
        	}
        	if (resourceReader.hasValue("BLOCKED_ENTRANCES")) {
        		for (String dungeonList : resourceReader.getArrayOfValues("BLOCKED_ENTRANCES")) {
        			String[] entrances = ResourceReader.toArray(dungeonList);
        			for (String entrance : entrances) {
        				preset.block(Direction.fromAbbreviation(entrance));
        			}
        		}
        	}
        	locationPresets.put(preset.getID(), preset);
        }
        resourceReader.close();
        
        locationPresets = Collections.unmodifiableMap(locationPresets);
        
        DLogger.info("Loaded " + locationPresets.size() + " location presets.");
    }

    private static void loadAchievements() {
        ACHIEVEMENTS = new HashMap<ID, Achievement>();
        
		ResourceReader reader = new ResourceReader("achievements.txt");
        
        while (reader.readNextElement()) {
        	AchievementBuilder builder = new AchievementBuilder();
        	builder.setID(reader.getValue("ID"));
        	builder.setName(reader.getValue("NAME"));
        	builder.setInfo(reader.getValue("INFO"));
        	builder.setText(reader.getValue("TEXT"));
        	
        	builder.setMinimumBattleCount(readIntegerFromResourceReader(reader, "MINIMUM_BATTLE_COUNT"));
        	builder.setLongestBattleLength(readIntegerFromResourceReader(reader, "LONGEST_BATTLE_LENGTH"));
        	
        	CounterMap<ID> killsByCreatureId = readIDCounterMap(reader, "KILLS_BY_CREATURE_ID");
            builder.setKillsByCreatureID(killsByCreatureId);
        	
            CounterMap<String> killsByCreatureType = readStringCounterMap(reader, "KILLS_BY_CREATURE_TYPE");
            builder.setKillsByCreatureType(killsByCreatureType);
            
            CounterMap<CauseOfDeath> killsByCauseOfDeath = new CounterMap<CauseOfDeath>();
            if (reader.hasValue("KILLS_BY_CAUSE_OF_DEATH")) {
            	String[] arrayOfCausesOfDeath = reader.getArrayOfValues("KILLS_BY_CAUSE_OF_DEATH");
            	for (String dungeonList : arrayOfCausesOfDeath) {
            		String[] elements = ResourceReader.toArray(dungeonList);
            		TypeOfCauseOfDeath typeOfCauseOfDeath = TypeOfCauseOfDeath.valueOf(elements[0]);
            		ID id = new ID(elements[1]);
            		int amount = Integer.parseInt(elements[2]);
            		killsByCauseOfDeath.incrementCounter(new CauseOfDeath(typeOfCauseOfDeath, id), amount);
            	}
            	builder.setKillsByCauseOfDeath(killsByCauseOfDeath);
            }
            
            CounterMap<ID> killsByLocationId = readIDCounterMap(reader, "KILLS_BY_LOCATION_ID");
            builder.setKillsByLocationID(killsByLocationId);
            
            CounterMap<ID> visitedLocations = readIDCounterMap(reader, "VISITED_LOCATIONS");
            builder.setVisitedLocations(visitedLocations);
            
            CounterMap<ID> maximumNumberOfVisits = readIDCounterMap(reader, "MAXIMUM_NUMBER_OF_VISITS");
            builder.setMaximumNumberOfVisits(maximumNumberOfVisits);
            
            Achievement achievement = builder.createAchievement();
        	
        	ACHIEVEMENTS.put(achievement.getID(), achievement);
        }

        reader.close();
        DLogger.info("Created " + ACHIEVEMENTS.size() + " achievements.");
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
    
    private static CounterMap<String> readStringCounterMap(ResourceReader reader, String key) {
    	CounterMap<String> counterMap = new CounterMap<String>();
    	if (reader.hasValue(key)) {
    		try {
    			String[] values = reader.getArrayOfValues(key);
    			for (String dungeonList : values) {
    				String[] parts = ResourceReader.toArray(dungeonList);
    				counterMap.incrementCounter(parts[0].trim(), Integer.parseInt(parts[1].trim()));
    			}
    		} catch (NumberFormatException log) {
    			DLogger.warning("Could not parse the value of " + key + ".");
    		}
    	}
    	return counterMap;
    }
    
    private static CounterMap<ID> toIDCounterMap(CounterMap<String> stringCounterMap) {
    	CounterMap<ID> idCounterMap = new CounterMap<ID>();
    	for (String key : stringCounterMap.keySet()) {
    		idCounterMap.incrementCounter(new ID(key), stringCounterMap.getCounter(key));
    	}
    	return idCounterMap;
    }
    
    private static CounterMap<ID> readIDCounterMap(ResourceReader reader, String key) {
    	return toIDCounterMap(readStringCounterMap(reader, key));
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
    
    private static Set<Item.Tag> itemTagSetFromArray(String[] strings) {
    	Set<Item.Tag> set = new HashSet<Item.Tag>();
    	if (strings.length > 0) {
    		for (String tag : strings) {
    			try {
    				set.add(Item.Tag.valueOf(tag));
    			} catch (IllegalArgumentException fatal) {
    				String message = "Invalid tag '" + tag + "' found!";
    				DLogger.warning(message);
    				throw new Error(message, fatal);
    			}
    		}
    	} else {
    		DLogger.warning("Empty array used to create a Tag Set! Using empty set.");
    	}
    	return set;
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
    	ResourceReader reader = new ResourceReader("tutorial.txt");
    	reader.readNextElement();
    	tutorial = reader.getValue("TUTORIAL");
    	reader.close();
    }
    
    public static Map<ID, Creature> getCreatureModels() {
    	return creatures;
    }
    
    public static Map<ID, ItemBlueprint> getItemBlueprints() {
    	return itemBlueprints;
    }
    
    public static Map<ID, SkillDefinition> getSkillDefinitions() {
    	return skillDefinitions;
    }
    
    public static Map<ID, LocationPreset> getLocationPresets() {
    	return locationPresets;
    }

}
