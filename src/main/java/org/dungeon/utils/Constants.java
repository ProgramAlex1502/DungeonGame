package main.java.org.dungeon.utils;

import java.awt.Color;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Constants {
	
	//TODO: finish Constants class
	
	public static final int COLS = 100;
	public static final String ACHIEVEMENT_UNLOCKED = "Achievement Unlocked!";
	
	public static final String SUICIDE_ATTEMPT_1 = "You cannot attempt suicide.";
	public static final String SUICIDE_ATTEMPT_2 = "You cannot target yourself.";
	
	public static final String LIST_ENTRY_FORMAT = "%-15s%s";
	
	public static final String INVENTORY_FULL = "Inventory is full.";
	
	public static final String NOT_EQUIPPING_A_WEAPON = "You are not equipping a weapon.";
	public static final String CANT_SEE_ANYTHING = "It's too dark, you can't see anything.";
	public static final String HERO_ID = "HERO";
	
	public static final DateTimeFormatter TIME_FORMAT = DateTimeFormat.forPattern("HH:mm:ss");
	public static final DateTimeFormatter DATE_FORMAT = DateTimeFormat.forPattern("dd/MM/yyyy");
	public static final Color FORE_COLOR_NORMAL = Color.LIGHT_GRAY;
	public static final Color FORE_COLOR_DARKER = Color.GRAY;
	public static final Color DEFAULT_BACK_COLOR = Color.BLACK;
	public static final Color HEALTH_BAR_COLOR = Color.GREEN;
	
	public static final int BAR_NAME_LENGTH = 16;
	public static final String UNARMED_ID = "";


}
