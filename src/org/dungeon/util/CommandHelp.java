package org.dungeon.util;

import java.util.List;

import org.dungeon.game.Command;
import org.dungeon.game.Game;
import org.dungeon.game.IssuedCommand;
import org.dungeon.io.IO;

public class CommandHelp {
	
	private CommandHelp() {
		
	}
	
	private static String noCommandStartsWith(String providedString) {
		return "No command starts with '" + providedString + "'.";
	}
	
	public static void printHelp(IssuedCommand issuedCommand) {
		if (issuedCommand.hasArguments()) {
			List<Command> commandList = Game.getCommandList();
			Command selectedCommand = null;
			for (Command command : commandList) {
				if (Utils.startsWithIgnoreCase(command.name, issuedCommand.getFirstArgument())) {
					if (selectedCommand == null) {
						selectedCommand = command;
					} else {
						Messenger.printAmbiguousSelectionMessage();
						return;
					}
				}
			}
			if (selectedCommand == null) {
				IO.writeString(noCommandStartsWith(issuedCommand.getFirstArgument()));
			} else {
				IO.writeString(selectedCommand.name + " (Command) " + '\n' + selectedCommand.info);
			}
		} else {
			Messenger.printMissingArgumentsMessage();
		}
	}
	
	public static void printCommandList(IssuedCommand issuedCommand) {
		String filter = null;
		if (issuedCommand.hasArguments()) {
			filter = issuedCommand.getFirstArgument();
		}
		List<Command> commandList = Game.getCommandList();
		
		final int CHARACTERS_PER_LIST_ENTRY = 80;
		final int NAME_COLUMN_WIDTH = 20;
		StringBuilder builder = new StringBuilder(CHARACTERS_PER_LIST_ENTRY * commandList.size());
		for (Command command : Game.getCommandList()) {
			if (filter == null || Utils.startsWithIgnoreCase(command.name, filter)) {
				builder.append(Utils.padString(command.name, NAME_COLUMN_WIDTH));
				builder.append(command.info);
				builder.append('\n');
			}
		}
		if (builder.length() == 0) {
			IO.writeString(noCommandStartsWith(issuedCommand.getFirstArgument()));
		}
		IO.writeString(builder.toString());
	}

}
