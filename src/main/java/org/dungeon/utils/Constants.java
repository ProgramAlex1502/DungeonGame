package main.java.org.dungeon.utils;

import java.awt.Color;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Constants {
	
	//TODO: finish Constants class
	
	public static final int COLS = 100;
	
	public static final String LIST_ENTRY_FORMAT = "%-15s%s";
	
	public static final String INVENTORY_FULL = "Inventory is full.";
	
	public static final String CANT_SEE_ANYTHING = "It's too dark, you can't see anything.";
	public static final String HERO_ID = "HERO";
	
	public static final DateTimeFormatter TIME_FORMAT = DateTimeFormat.forPattern("HH:mm:ss");
	
	public static final Color FORE_COLOR_NORMAL = Color.LIGHT_GRAY;
	public static final Color FORE_COLOR_DARKER = Color.GRAY;
	
	public static final String UNARMED_ID = "";

}
