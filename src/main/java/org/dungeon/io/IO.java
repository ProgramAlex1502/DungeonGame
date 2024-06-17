package main.java.org.dungeon.io;

import java.awt.*;

import main.java.org.dungeon.game.Game;
import main.java.org.dungeon.utils.Constants;
import main.java.org.dungeon.utils.Utils;

public final class IO {
	
	//TODO: finish IO class
	
	public static final long WRITE_BATTLE_STRING_WAIT = 300;
	
	public static void writeString(String string) {
		writeString(string, Constants.FORE_COLOR_NORMAL);
	}
	
	public static void writeString(String string, Color color) {
		writeString(string, color, true);
	}
	
	public static void writeString(String string, Color color, boolean newLine) {
		if (newLine) {
			Game.getGameWindow().writeToTextPane(Utils.clearEnd(string) + '\n', color, 0);
		} else {
			Game.getGameWindow().writeToTextPane(Utils.clearEnd(string), color, 0);
		}
	}
	
	public static void writeString(String string, Color color, boolean newLine, long wait) {
		if (color == null) {
			throw new IllegalArgumentException("color should not be null.");
		}
		
		if (newLine) {
			Game.getGameWindow().writeToTextPane(Utils.clearEnd(string) + '\n', color, wait);
		} else {
			Game.getGameWindow().writeToTextPane(Utils.clearEnd(string), color, wait);
		}
	}
	
	public static void writeBattleString(String string, Color color) {
		writeString(string, color, true, WRITE_BATTLE_STRING_WAIT);
	}
	
	public static void writeNewLine() {
		writeString("");
	}
	
	public static void writeKeyValueString(String key, String value) {
		writeKeyValueString(key, value, Constants.FORE_COLOR_NORMAL, Constants.FORE_COLOR_DARKER);
	}
	
	public static void writeKeyValueString(String key, String value, Color textColor, Color fillColor) {
		int dots = Constants.COLS - key.length() - value.length();
		
		if (dots < 0) {
			throw new IllegalArgumentException("strings are too large");
		}
		
		writeString(key, textColor, false);
		
		StringBuilder stringBuilder = new StringBuilder();
		for(; dots > 0; dots--) {
			stringBuilder.append('.');
		}
		
		writeString(stringBuilder.toString(), fillColor, false);
		writeString(value, textColor, true);
	}

}
