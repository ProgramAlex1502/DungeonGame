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

}
