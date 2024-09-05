package org.dungeon.util;

import java.awt.Color;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.dungeon.game.ID;

public class Constants {
	
	public static final int COLS = 100;
	
	public static final String INVALID_INPUT = "Invalid input.";
	
	public static final DateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
	public static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
	public static final Color FORE_COLOR_NORMAL = Color.LIGHT_GRAY;
	
	public static final ID HERO_ID = new ID("HERO");

}
