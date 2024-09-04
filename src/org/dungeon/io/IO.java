package org.dungeon.io;

import java.awt.Color;
import java.util.List;

import org.dungeon.achievements.Achievement;
import org.dungeon.date.Period;
import org.dungeon.game.Game;
import org.dungeon.util.Constants;
import org.dungeon.util.Poem;
import org.dungeon.util.Utils;

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
	
	private static void writeString(String string, Color color, boolean newLine, boolean scrollDown) {
		writeString(string, color, newLine, scrollDown, 0);
	}
	
	private static void writeString(String string, Color color, boolean newLine, boolean scrollDown, int wait) {
		if (color == null) {
			DLogger.warning("Passed null as a Color to writeString.");
		}
		
		String processedString = newLine ? Utils.clearEnd(string) + '\n' : Utils.clearEnd(string);
		Game.getGameWindow().writeToTextPane(processedString, color, scrollDown);

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
	
	public static void writeKeyValueString(String key, String value, Color textColor, Color fillColor) {
		int dots = Constants.COLS - key.length() - value.length();
		if (dots < 0) {
			DLogger.warning("Passed too large strings to writeKeyValueString.");
		}
		writeString(key, textColor, false);
		StringBuilder stringBuilder = new StringBuilder();
		for (; dots > 0; dots--) {
			stringBuilder.append('.');
		}
		
		writeString(stringBuilder.toString(), fillColor, false);
		writeString(value, textColor, true);
	}
	
	public static void writePoem(Poem poem) {
		writeString(poem.toString(), Constants.FORE_COLOR_NORMAL, false, false);
	}
	
	public static void writeAchievementList(List<Achievement> achievementList) {
		writeAchievementList(achievementList, null);
	}
	
	public static void writeAchievementList(List<Achievement> achievementList, List<Period> timeSinceUnlockList) {
		for (int i = 0; i < achievementList.size(); i++) {
			Achievement achievement = achievementList.get(i);
			String achievementName = achievement.getName();
			if (timeSinceUnlockList != null) {
				Period timeSinceUnlock = timeSinceUnlockList.get(i);
				achievementName += " (" + timeSinceUnlock + " ago)";
			}
			IO.writeString(achievementName, Color.ORANGE);
			IO.writeString(" " + achievement.getInfo(), Color.YELLOW);
		}
	}

}
