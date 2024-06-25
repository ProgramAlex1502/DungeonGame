package main.java.org.dungeon.io;

import java.awt.Color;

import main.java.org.dungeon.game.Game;
import main.java.org.dungeon.util.Constants;
import main.java.org.dungeon.util.Percentage;
import main.java.org.dungeon.util.Poem;
import main.java.org.dungeon.util.Utils;

public final class IO {
		
	private static final long WRITE_BATTLE_STRING_WAIT = 300;
	
	public static void writeString(String string) {
		writeString(string, Constants.FORE_COLOR_NORMAL);
	}
	
	public static void writeString(String string, Color color) {
		writeString(string, color, true);
	}
	
	public static void writeString(String string, Color color, boolean newLine) {
		if (newLine) {
			Game.getGameWindow().writeToTextPane(Utils.clearEnd(string) + '\n', color);
		} else {
			Game.getGameWindow().writeToTextPane(Utils.clearEnd(string), color);
		}
	}
	
	public static void writeString(String string, Color color, boolean newLine, long wait) {
		if (color == null) {
			DLogger.warning("Passed null as a Color to writeString.");
		}
		
		if (newLine) {
			Game.getGameWindow().writeToTextPane(Utils.clearEnd(string) + '\n', color);
		} else {
			Game.getGameWindow().writeToTextPane(Utils.clearEnd(string), color);
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
	
	private static void writeKeyValueString(String key, String value, Color textColor, Color fillColor) {
		int dots = Constants.COLS - key.length() - value.length();
		
		if (dots < 0) {
			DLogger.warning("Passed too large strings to writeKeyValueString.");
		}
		
		writeString(key, textColor, false);
		
		StringBuilder stringBuilder = new StringBuilder();
		for(; dots > 0; dots--) {
			stringBuilder.append('.');
		}
		
		writeString(stringBuilder.toString(), fillColor, false);
		writeString(value, textColor, true);
	}
	
	public static void writePoem(Poem poem) {
		writeString(poem.getTitle() + "\n\n" + poem.getContent() + "\n\n" + poem.getAuthor());
	}
	
	public static void writeNamedBar(String name, Percentage percentage, Color fore) {
		if (name.length() > Constants.BAR_NAME_LENGTH) {
			DLogger.warning("Passed a too long bar name.");
		}
		
		writeString(name, Constants.FORE_COLOR_NORMAL, false);
		int size = Constants.COLS - Constants.BAR_NAME_LENGTH;
		
		int bars = (int) (size * percentage.toDouble()) + 1;
		
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
