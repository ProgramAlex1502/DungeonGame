package main.java.org.dungeon.game;

import java.awt.Color;

import main.java.org.dungeon.io.IO;
import main.java.org.dungeon.util.Utils;

public class ConfigTools {
	
	private static final String[] args = {"bold", "generator", "list"};
	
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
				toggleBold();
				return true;
			} else if (issuedCommand.firstArgumentEquals(args[1])) {
				if (issuedCommand.getArguments().length > 1) {
					return changeChunkSide(issuedCommand.getArguments()[1]);
				} else {
					IO.writeString("Provide a numerical argument.");
				}
			} else if (issuedCommand.firstArgumentEquals(args[2])) {
				listAllArguments();
			} else {
				IO.writeString("Invalid command. Use 'config list' to see all available configurations.", Color.RED);
			}
		} else {
			Utils.printMissingArgumentsMessage();
		}
		
		return false;
	}

}
