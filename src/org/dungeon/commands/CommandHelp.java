package org.dungeon.commands;

import java.util.List;

import org.dungeon.game.Game;
import org.dungeon.io.IO;
import org.dungeon.util.Messenger;
import org.dungeon.util.Utils;

public final class CommandHelp {
	
	private static final int COMMAND_NAME_COLUMN_WIDTH = 20;
	
	private CommandHelp() {
		throw new AssertionError();
	}
	
	private static String noCommandStartsWith(String providedString) {
		return "No command starts with '" + providedString + "'.";
	}
	
	public static void printHelp(IssuedCommand issuedCommand) {
		if (issuedCommand.hasArguments()) {
			List<CommandDescription> commandDescriptionList = Game.getCommandDescriptions();
			CommandDescription selectedCommand = null;
			for (CommandDescription description : commandDescriptionList) {
				if (Utils.startsWithIgnoreCase(description.getName(), issuedCommand.getFirstArgument())) {
					if (selectedCommand == null) {
						selectedCommand = description;
					} else {
						Messenger.printAmbiguousSelectionMessage();
						return;
					}
				}
			}
			if (selectedCommand == null) {
				IO.writeString(noCommandStartsWith(issuedCommand.getFirstArgument()));
			} else {
				IO.writeString(selectedCommand.toString());
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
		
		List<CommandDescription> commandList = Game.getCommandDescriptions();
		StringBuilder builder = new StringBuilder();
		for (CommandDescription command : commandList) {
			if (filter == null || Utils.startsWithIgnoreCase(command.getName(), filter)) {
				builder.append(Utils.padString(command.getName(), COMMAND_NAME_COLUMN_WIDTH));
				builder.append(command.getInfo());
				builder.append('\n');
			}
		}
		if (builder.length() == 0) {
			IO.writeString(noCommandStartsWith(issuedCommand.getFirstArgument()));
		} else {
			IO.writeString(builder.toString());
		}
	}

}
