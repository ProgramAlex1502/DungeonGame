package main.java.org.dungeon.util;

import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import main.java.org.dungeon.game.ID;

public class Constants {
		
	public static final int COLS = 100;
	public static final String FILE_FOUND = "A saved campaign was found.";
	public static final String ACHIEVEMENT_UNLOCKED = "Achievement Unlocked!";
	
	public static final String SUICIDE_ATTEMPT_1 = "You cannot attempt suicide.";
	public static final String SUICIDE_ATTEMPT_2 = "You cannot target yourself.";
	
	public static final String INVALID_INPUT = "Invalid input.";
	
	public static final String COMMAND_HELP_FORMAT = "%-20s %s";
	public static final String LIST_ENTRY_FORMAT = "%-15s%s";
	
	public static final String INVENTORY_FULL = "Inventory is full.";
	
	public static final String NOT_EQUIPPING_A_WEAPON = "You are not equipping a weapon.";
	public static final String CANT_SEE_ANYTHING = "It's too dark, you can't see anything.";
	public static final String INVALID_COMMAND = "'%s' is not a command.";
	public static final String SUGGEST_COMMANDS = "See 'commands' for a list of commands.";
	
	public static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
	public static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
	public static final Color FORE_COLOR_NORMAL = Color.LIGHT_GRAY;
	public static final Color FORE_COLOR_DARKER = Color.GRAY;
	public static final Color HEALTH_BAR_COLOR = Color.GREEN;
	
	public static final int BAR_NAME_LENGTH = 16;
	
	public static final ID HERO_ID = new ID("HERO");
	public static final ID UNARMED_ID = new ID("");
	
	public static final String NAME = "Dungeon";


}
