package main.java.org.dungeon.wiki;

import main.java.org.dungeon.game.IssuedCommand;
import main.java.org.dungeon.io.IO;
import main.java.org.dungeon.util.Utils;

public class Wiki {
	
	public static void search(IssuedCommand issuedCommand) {
		if (issuedCommand.hasArguments()) {
			IO.writeString("Wiki not yet implemented.");
		} else {
			Utils.printMissingArgumentsMessage();
		}
	}

}
