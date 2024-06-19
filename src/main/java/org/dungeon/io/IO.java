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
	
	public static void writeNamedBar(String name, double percentage, Color fore) {
		if (percentage < 0.0 || percentage > 1.0) {
			throw new IllegalArgumentException("percentage must be in the range [0.0, 1.0]");
		}
		
		if (name.length() > Constants.BAR_NAME_LENGTH) {
			throw new IllegalArgumentException("name is too long.");
		}
		
		writeString(name, Constants.FORE_COLOR_NORMAL, false);
		int size = Constants.COLS - Constants.BAR_NAME_LENGTH;
		
		int bars = (int) (size * percentage) + 1;
		
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < Constants.BAR_NAME_LENGTH - name.length(); i++) {
			sb.append(' ');
		}
		
		for(int i = 0; i < size; i++) {
			if (i < bars) {
				sb.append('|');
			} else {
				sb.append(' ');
			}
		}
		
		writeString(sb.toString(), fore, true);
	}

}
