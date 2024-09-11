package org.dungeon.commands;

import java.util.List;

import org.dungeon.game.Name;
import org.dungeon.game.QuantificationMode;
import org.dungeon.io.IO;
import org.dungeon.util.Messenger;
import org.dungeon.util.Utils;

final class CommandHelp {
	
	private static final int COMMAND_NAME_COLUMN_WIDTH = 20;
	private static final Name COMMAND_NAME = Name.newInstance("command");
	
	private CommandHelp() {
		throw new AssertionError();
	}
	
	private static String noCommandStartsWith(String providedString) {
		return "No command starts with '" + providedString + "'.";
	}
	
	public static void printHelp(IssuedCommand issuedCommand) {
		if (issuedCommand.hasArguments()) {
			List<CommandDescription> descriptions = CommandCollection.getDefaultCommandCollection().getCommandDescriptions();
			CommandDescription selectedCommand = null;
			for (CommandDescription description : descriptions) {
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
		
		List<CommandDescription> descriptions = CommandCollection.getDefaultCommandCollection().getCommandDescriptions();
		StringBuilder builder = new StringBuilder();
		int count = 0;
		for (CommandDescription description : descriptions) {
			if (filter == null || Utils.startsWithIgnoreCase(description.getName(), filter)) {
				count++;
				builder.append(Utils.padString(description.getName(), COMMAND_NAME_COLUMN_WIDTH));
				builder.append(description.getInfo());
				builder.append('\n');
			}
		}
		if (count == 0) {
			IO.writeString(noCommandStartsWith(issuedCommand.getFirstArgument()));
		} else {
			if (count > 1) {
				String quantifiedName = COMMAND_NAME.getQuantifiedName(count, QuantificationMode.NUMBER);
				builder.append("\nListed ").append(quantifiedName).append(".");
				if (filter == null) {
					builder.append("\nYou can filter the output of this command by typing the beginning of the desired command.");
				}
			}
			IO.writeString(builder.toString());
		}
	}

}
