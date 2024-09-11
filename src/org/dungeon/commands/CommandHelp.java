package org.dungeon.commands;

import java.util.List;

import org.dungeon.io.IO;
import org.dungeon.util.Messenger;
import org.dungeon.util.Utils;

final class CommandHelp {
	
	private static final int COMMAND_NAME_COLUMN_WIDTH = 20;
	
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
		for (CommandDescription description : descriptions) {
			if (filter == null || Utils.startsWithIgnoreCase(description.getName(), filter)) {
				builder.append(Utils.padString(description.getName(), COMMAND_NAME_COLUMN_WIDTH));
				builder.append(description.getInfo());
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
