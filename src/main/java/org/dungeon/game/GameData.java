package main.java.org.dungeon.game;

import java.awt.Font;
import java.util.HashMap;

import main.java.org.dungeon.achievements.Achievement;
import main.java.org.dungeon.creatures.CreatureBlueprint;
import main.java.org.dungeon.items.ItemBlueprint;

public class GameData {
	
	//TODO: finish GameData class
	
	public static HashMap<String, CreatureBlueprint> CREATURE_BLUEPRINTS;
	public static HashMap<String, ItemBlueprint> ITEM_BLUEPRINTS;
	public static LocationPreset[] LOCATION_PRESETS;
	public static HashMap<String, Achievement> ACHIEVEMENTS;
	
	public static Font monospaced;
	
	public static String LICENSE;
	
	public static LocationPreset getRandomRiver() {
		LocationPreset river = new LocationPreset("River");
		river.block(Direction.EAST).block(Direction.WEST);
		river.setLightPermittivity(1.0);
		return river;
	}
	
	public static LocationPreset getRandomBridge() {
		LocationPreset bridge = new LocationPreset("Bridge");
		bridge.block(Direction.SOUTH).block(Direction.NORTH);
		bridge.setLightPermittivity(1.0);
		return bridge;
	}

}
