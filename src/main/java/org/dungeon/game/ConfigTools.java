package main.java.org.dungeon.game;

import java.awt.Color;

import main.java.org.dungeon.io.IO;
import main.java.org.dungeon.utils.Utils;

public class ConfigTools {
	
	private static final String[] args = {"bars", "bold", "generator", "list", "rows"};
	
	private static final int MIN_ROWS = 10;
	private static final int MAX_ROWS = 50;
	
	private static void listAllArguments() {
		IO.writeString("Arguments are: ");
		for (String arg : args) {
			IO.writeString("\n  " + arg);
		}
	}
	
	private static void toggleBold() {
		boolean newBoldValue = !Game.getGameState().isBold();
		Game.getGameState().setBold(newBoldValue);
		IO.writeString("Bold set to " + newBoldValue + ".");
	}
	
	private static void toggleBars() {
		boolean newUsingBarsValue = !Game.getGameState().isUsingBars();
		Game.getGameState().setUsingBars(newUsingBarsValue);
		if (newUsingBarsValue) {
			IO.writeString("Bars enabled.");
		} else {
			IO.writeString("Bars disabled.");
		}
	}
	
	private static boolean changeRowCount(String argument) {
		try {
			int rows = Integer.parseInt(argument);
			if (rows < MIN_ROWS || rows > MAX_ROWS) {
				IO.writeString("Row count should be in the range [" + MIN_ROWS + ", " + MAX_ROWS + "].");
			} else {
				if (Game.getGameWindow().setRows(rows)) {
					IO.writeString("Rows set to " + rows + ".");
					return true;
				} else {
					IO.writeString("Row count unchanged.");
				}
			}
		} catch (NumberFormatException exception) {
			IO.writeString("Provide a valid number of rows.", Color.RED);
		}
		return false;
	}
	
	private static boolean changeChunkSide(String argument) {
		try {
			int givenSide = Integer.parseInt(argument);
			int oldChunkSide = Game.getGameState().getWorld().getGenerator().getChunkSide();
			
			int newChunkSide = Game.getGameState().getWorld().getGenerator().setChunkSide(givenSide);
			
			if (oldChunkSide == newChunkSide) {
				IO.writeString("Chunk side unchanged.");
			} else {
				IO.writeString("Chunk side set to " + newChunkSide + ".");
				return true;
			}
		} catch (NumberFormatException exception) {
			IO.writeString("Provide a valid number.", Color.RED);
		}
		
		return false;
	}
	
	static boolean parseConfigCommand(IssuedCommand issuedCommand) {
		if (issuedCommand.hasArguments()) {
			if (issuedCommand.firstArgumentEquals(args[0])) {
				toggleBars();
				return true;
			} else if (issuedCommand.firstArgumentEquals(args[1])) {
				toggleBold();
				return true;
			} else if (issuedCommand.firstArgumentEquals(args[2])) {
				if (issuedCommand.getArguments().length > 1) {
					return changeChunkSide(issuedCommand.getArguments()[1]);
				} else {
					IO.writeString("Provide a numerical argument.");
				}
			} else if (issuedCommand.firstArgumentEquals(args[3])) {
				listAllArguments();
			} else if (issuedCommand.firstArgumentEquals(args[4])) {
				if (issuedCommand.getArguments().length > 1) {
					return changeRowCount(issuedCommand.getArguments()[1]);
				} else {
					IO.writeString("Provide a number of rows.");
				}
			} else {
				IO.writeString("Invalid command. Use 'config list' to see all available configurations.", Color.RED);
			}
		} else {
			Utils.printMissingArgumentsMessage();
		}
		
		return false;
	}

}
