package org.dungeon.io;

import java.awt.Color;

import org.dungeon.game.Game;
import org.dungeon.util.Constants;
import org.dungeon.util.Poem;

public class IO {
	
	private static final int WRITE_BATTLE_STRING_WAIT = 300;
	
	private IO() {
		throw new AssertionError();
	}
	
	public static void writeString(String string) {
		writeString(string, Constants.FORE_COLOR_NORMAL);
	}
	
	public static void writeString(String string, Color color) {
		writeString(string, color, true);
	}
	
	public static void writeString(String string, Color color, boolean newLine) {
		writeString(string, color, newLine, true, 0);
	}
	
	private static void writeString(String string, Color color, boolean newLine, boolean scrollDown, int wait) {
		if (color == null) {
			DLogger.warning("Passed null as a Color to writeString.");
		}
		if (newLine) {
			string += '\n';
		}
		
		Game.getGameWindow().writeToTextPane(string, color, scrollDown);

		if (wait > 0) {
			Sleeper.sleep(wait);
		}
	}
	
	public static void writeBattleString(String string, Color color) {
		writeString(string, color, true, true, WRITE_BATTLE_STRING_WAIT);
	}
	
	public static void writeNewLine() {
		writeString("");
	}
	
	public static void writePoem(Poem poem) {
		writeString(poem.toString(), Constants.FORE_COLOR_NORMAL, false, false, 0);
	}

}
