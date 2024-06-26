package main.java.org.dungeon.game;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import main.java.org.dungeon.achievements.Achievement;
import main.java.org.dungeon.achievements.AchievementBuilder;
import main.java.org.dungeon.creatures.CreatureBlueprint;
import main.java.org.dungeon.io.DLogger;
import main.java.org.dungeon.io.ResourceReader;
import main.java.org.dungeon.items.ItemBlueprint;
import main.java.org.dungeon.skill.SkillDefinition;
import main.java.org.dungeon.util.CounterMap;
import main.java.org.dungeon.util.StopWatch;

public final class GameData {
	
	public static final Font FONT = getMonospacedFont();
	private static final PoetryLibrary poetryLibrary = new PoetryLibrary();
	private static final DreamLibrary dreamLibrary = new DreamLibrary();
	private static final HintLibrary hintLibrary = new HintLibrary();
	private static final String COUNTER_MAP_KEY_VALUE_SEPARATOR = ",";

	public static HashMap<ID, Achievement> ACHIEVEMENTS;
	public static String LICENSE;
	private static Map<ID, CreatureBlueprint> creatureBlueprints = new HashMap<ID, CreatureBlueprint>();
	private static Map<ID, ItemBlueprint> itemBlueprints = new HashMap<ID, ItemBlueprint>();
	private static Map<ID, SkillDefinition> skillDefinitions = new HashMap<ID, SkillDefinition>();
	private static Map<ID, LocationPreset> locationPresets = new HashMap<ID, LocationPreset>();
	
	public static PoetryLibrary getPoetryLibrary() {
		return poetryLibrary;
	}
	
	public static DreamLibrary getDreamLibrary() {
		return dreamLibrary;
	}
	
	public static HintLibrary getHintLibrary() {
		return hintLibrary;
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
	
	private static Font getMonospacedFont() {
		final int FONT_SIZE = 15;
		Font font = new Font(Font.MONOSPACED, Font.PLAIN, FONT_SIZE);
		try {
			InputStream fontStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("DroidSansMono.ttf");
			font = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(Font.PLAIN, FONT_SIZE);
		} catch (FontFormatException bad) {
			DLogger.warning(bad.getMessage());
			Game.exit();
		} catch (IOException bad) {
			DLogger.warning(bad.getMessage());
			Game.exit();
		}
		
		return font;
	}
	
	private static void createSkills() {
		SkillDefinition fireball = new SkillDefinition("FIREBALL", "Skill", "Fireball", 10, 6);
		skillDefinitions.put(fireball.getID(), fireball);
		
		SkillDefinition burningGround = new SkillDefinition("BURNING_GROUND", "Skill", "Burning Ground", 18, 12);
		skillDefinitions.put(burningGround.getID(), burningGround);
		skillDefinitions = Collections.unmodifiableMap(skillDefinitions);
	}
	
	private static void loadItemBlueprints() {
		ResourceReader resourceReader = new ResourceReader("items.txt");
		
		while (resourceReader.readNextElement()) {
			ItemBlueprint blueprint = new ItemBlueprint();
			blueprint.setID(new ID(resourceReader.getValue("ID")));
			blueprint.setType(resourceReader.getValue("TYPE"));
			blueprint.setName(resourceReader.getValue("NAME"));
		    blueprint.setCurIntegrity(Integer.parseInt(resourceReader.getValue("CUR_INTEGRITY")));
		    blueprint.setMaxIntegrity(Integer.parseInt(resourceReader.getValue("MAX_INTEGRITY")));
		    blueprint.setRepairable(Integer.parseInt(resourceReader.getValue("REPAIRABLE")) == 1);
		    blueprint.setWeapon(Integer.parseInt(resourceReader.getValue("WEAPON")) == 1);
		    blueprint.setDamage(Integer.parseInt(resourceReader.getValue("DAMAGE")));
		    blueprint.setHitRate(Double.parseDouble(resourceReader.getValue("HIT_RATE")));
		    blueprint.setIntegrityDecrementOnHit(Integer.parseInt(resourceReader.getValue("INTEGRITY_DECREMENT_ON_HIT")));
		    blueprint.setFood(Integer.parseInt(resourceReader.getValue("FOOD")) == 1);
		    if (resourceReader.hasValue("NUTRITION")) {
		    	blueprint.setNutrition(Integer.parseInt(resourceReader.getValue("NUTRITION")));
		    }
		    if (resourceReader.hasValue("INTEGRITY_DECREMENT_ON_EAT")) {
		        blueprint.setIntegrityDecrementOnEat(Integer.parseInt(resourceReader.getValue("INTEGRITY_DECREMENT_ON_EAT")));
		    }
		    blueprint.setClock(Integer.parseInt(resourceReader.getValue("CLOCK")) == 1);
		    blueprint.setBook(Integer.parseInt(resourceReader.getValue("BOOK")) == 1);
		    if (resourceReader.hasValue("SKILL")) {
		    	blueprint.setSkill(resourceReader.getValue("SKILL"));
		    }
			
		    itemBlueprints.put(blueprint.getID(), blueprint);
		}
		
		resourceReader.close();
		itemBlueprints = Collections.unmodifiableMap(itemBlueprints);
		DLogger.info("Loaded " + itemBlueprints.size() + " item blueprints");
	}
	
	private static void loadCreatureBlueprints() {        
        ResourceReader resourceReader = new ResourceReader("creatures.txt");
        
        while (resourceReader.readNextElement()) {
        	CreatureBlueprint blueprint = new CreatureBlueprint();
        	blueprint.setID(new ID(resourceReader.getValue("ID")));
        	blueprint.setType(resourceReader.getValue("TYPE"));
        	blueprint.setName(resourceReader.getValue("NAME"));
        	blueprint.setCurHealth(Integer.parseInt(resourceReader.getValue("CUR_HEALTH")));
        	blueprint.setMaxHealth(Integer.parseInt(resourceReader.getValue("MAX_HEALTH")));
        	blueprint.setAttack(Integer.parseInt(resourceReader.getValue("ATTACK")));
        	blueprint.setAttackAlgorithmID(resourceReader.getValue("ATTACK_ALGORITHM_ID"));
        	creatureBlueprints.put(blueprint.getID(), blueprint);
        }
        
        resourceReader.close();
        creatureBlueprints = Collections.unmodifiableMap(creatureBlueprints);
        DLogger.info("Loaded " + creatureBlueprints.size() + " creature blueprints.");
    }
	
	private static void loadLocationPresets() {
        SpawnerPreset bat = new SpawnerPreset("BAT", 1, 6);
        SpawnerPreset bear = new SpawnerPreset("BEAR", 1, 12);
        SpawnerPreset boar = new SpawnerPreset("BOAR", 3, 8);
        SpawnerPreset crocodile = new SpawnerPreset("CROCODILE", 1, 6);
        SpawnerPreset fox = new SpawnerPreset("FOX", 4, 4);
        SpawnerPreset frog = new SpawnerPreset("FROG", 2, 2);
        SpawnerPreset fruitBat = new SpawnerPreset("FRUIT_BAT", 2, 2);
        SpawnerPreset lizard = new SpawnerPreset("LIZARD", 1, 4);
        SpawnerPreset orc = new SpawnerPreset("ORC", 2, 2);
        SpawnerPreset rabbit = new SpawnerPreset("RABBIT", 8, 1);
        SpawnerPreset rat = new SpawnerPreset("RAT", 6, 2);
        SpawnerPreset skeleton = new SpawnerPreset("SKELETON", 1, 12);
        SpawnerPreset greenIguana = new SpawnerPreset("GREEN_IGUANA", 2, 32);
        SpawnerPreset snake = new SpawnerPreset("SNAKE", 3, 4);
        SpawnerPreset spider = new SpawnerPreset("SPIDER", 2, 4);
        SpawnerPreset whiteTiger = new SpawnerPreset("WHITE_TIGER", 1, 24);
        SpawnerPreset wolf = new SpawnerPreset("WOLF", 3, 12);
        SpawnerPreset zombie = new SpawnerPreset("ZOMBIE", 2, 4);

        LocationPreset clearing = new LocationPreset("CLEARING", "Land", "Clearing");
        clearing.addSpawner(frog).addSpawner(rabbit).addSpawner(spider).addSpawner(fox);
        clearing.addItem("CHERRY", 0.6).addItem("STICK", 0.9);
        clearing.setLightPermittivity(1.0);
        clearing.setBlobSize(2);
        clearing.finish();
        locationPresets.put(clearing.getID(), clearing);

        LocationPreset desert = new LocationPreset("DESERT", "Land", "Desert");
        desert.addSpawner(rat).addSpawner(snake).addSpawner(zombie).addSpawner(boar);
        desert.addItem("MACE", 0.1).addItem("STAFF", 0.2).addItem("DAGGER", 0.15).addItem("SPEAR", 0.1);
        desert.setLightPermittivity(1.0);
        desert.setBlobSize(50);
        desert.finish();
        locationPresets.put(desert.getID(), desert);

        LocationPreset forest = new LocationPreset("FOREST", "Land", "Forest");
        forest.addSpawner(bear).addSpawner(frog).addSpawner(rabbit).addSpawner(whiteTiger).addSpawner(fruitBat);
        forest.addItem("AXE", 0.2).addItem("POCKET_WATCH", 0.03).addItem("STICK", 0.5).addItem("TOME_OF_FIREBALL", 0.1);
        forest.setLightPermittivity(0.7);
        forest.setBlobSize(25);
        forest.finish();
        locationPresets.put(forest.getID(), forest);

        LocationPreset graveyard = new LocationPreset("GRAVEYARD", "Land", "Graveyard");
        graveyard.addSpawner(bat).addSpawner(skeleton).addSpawner(zombie).addSpawner(orc);
        graveyard.addItem("LONGSWORD", 0.15).addItem("WRIST_WATCH", 0.025).addItem("TOME_OF_BURNING_GROUND", 0.1);
        graveyard.setLightPermittivity(0.9);
        graveyard.setBlobSize(1);
        graveyard.finish();
        locationPresets.put(graveyard.getID(), graveyard);

        LocationPreset meadow = new LocationPreset("MEADOW", "Land", "Meadow");
        meadow.addSpawner(whiteTiger).addSpawner(wolf);
        meadow.addItem("STONE", 0.8).addItem("WATERMELON", 0.4).addItem("APPLE", 0.7);
        meadow.setLightPermittivity(1.0);
        meadow.setBlobSize(5);
        meadow.finish();
        locationPresets.put(meadow.getID(), meadow);

        LocationPreset pond = new LocationPreset("POND", "Land", "Pond");
        pond.addSpawner(frog).addSpawner(lizard).addSpawner(crocodile).addSpawner(greenIguana);
        pond.addItem("WATERMELON", 0.8).addItem("SPEAR", 0.3);
        pond.setLightPermittivity(0.96);
        pond.setBlobSize(1);
        pond.finish();
        locationPresets.put(pond.getID(), pond);

        LocationPreset swamp = new LocationPreset("SWAMP", "Land", "Swamp");
        swamp.addSpawner(frog).addSpawner(snake).addSpawner(lizard).addSpawner(crocodile);
        swamp.addItem("STICK", 0.9).addItem("WATERMELON", 0.12).addItem("CLUB", 0.4);
        swamp.setLightPermittivity(0.7);
        swamp.setBlobSize(10);
        swamp.finish();
        locationPresets.put(swamp.getID(), swamp);

        LocationPreset wasteland = new LocationPreset("WASTELAND", "Land", "Wasteland");
        wasteland.addSpawner(rat).addSpawner(spider).addSpawner(snake);
        wasteland.addItem("STONE", 0.3).addItem("STICK", 0.18);
        wasteland.setLightPermittivity(1.0);
        wasteland.setBlobSize(10);
        wasteland.finish();
        locationPresets.put(wasteland.getID(), wasteland);

        LocationPreset savannah = new LocationPreset("SAVANNAH", "Land", "Savannah");
        savannah.addSpawner(boar).addSpawner(snake).addSpawner(whiteTiger);
        savannah.addItem("APPLE", 0.8).addItem("AXE", 0.3);
        savannah.setLightPermittivity(1.0);
        savannah.setBlobSize(5);
        savannah.finish();
        locationPresets.put(savannah.getID(), savannah);

        LocationPreset river = new LocationPreset("RIVER", "River", "River");
        river.block(Direction.WEST).block(Direction.EAST);
        river.setLightPermittivity(1.0);
        river.finish();
        locationPresets.put(river.getID(), river);
        
        LocationPreset bridge = new LocationPreset("BRIDGE", "Bridge", "Bridge");
        bridge.addItem("TOME_OF_FIREBALL", 0.15);
        bridge.block(Direction.NORTH).block(Direction.SOUTH);
        bridge.setLightPermittivity(1.0);
        bridge.finish();
        locationPresets.put(bridge.getID(), bridge);
        
        locationPresets = Collections.unmodifiableMap(locationPresets);
        
        DLogger.info("Created " + locationPresets.size() + " location presets.");
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

        	CounterMap<ID> killsByCreatureID = readIDCounterMap(reader, "KILLS_BY_CREATURE_ID");
        	builder.setKillsByCreatureID(killsByCreatureID);

        	CounterMap<String> killsByCreatureType = readStringCounterMap(reader, "KILLS_BY_CREATURE_TYPE");
        	builder.setKillsByCreatureType(killsByCreatureType);
        	
        	CounterMap<ID> killsByWeapon = readIDCounterMap(reader, "KILLS_BY_WEAPON");
        	builder.setKillsByWeapon(killsByWeapon);
        	
        	CounterMap<ID> killsByLocationID = readIDCounterMap(reader, "KILLS_BY_LOCATION_ID");
        	builder.setKillsByLocationID(killsByLocationID);
        	
        	CounterMap<ID> discoveredLocations = readIDCounterMap(reader, "DISCOVERED_LOCATION");
        	builder.setDiscoveredLocations(discoveredLocations);
        	
        	CounterMap<ID> maximumNumberOfVisits = readIDCounterMap(reader, "MAXIMUM_NUMBER_OF_VISITS");
        	builder.setMaximumNumberOfVisits(maximumNumberOfVisits);
        	
        	Achievement achievement = builder.createAchievement();
        	ACHIEVEMENTS.put(achievement.getID(), achievement);
        }
        
        reader.close();
        DLogger.info("Created " + ACHIEVEMENTS.size() + " achievements.");
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
				for (String value : values) {
					String[] parts = value.split(COUNTER_MAP_KEY_VALUE_SEPARATOR);
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
	
	private static void loadLicense() {
        ResourceReader reader = new ResourceReader("license.txt");
        reader.readNextElement();
        LICENSE = reader.getValue("LICENSE");
        reader.close();
    }
	
	public static Map<ID, CreatureBlueprint> getCreatureBlueprints() {
		return creatureBlueprints;
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
