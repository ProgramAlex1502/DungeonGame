package org.dungeon.util;

import java.awt.Color;

import org.dungeon.io.IO;

public class Messenger {
	
	private Messenger() {
		throw new AssertionError();
	}
	
	public static void printInvalidCommandMessage(String command) {
		IO.writeString("'" + command + "' is not a recognized command.", Color.RED);
		IO.writeString("See 'commands' for a complete list of commands.", Color.ORANGE);
	}
	
	public static void printInvalidNumberFormarOrValue() {
		IO.writeString("Invalid number format or value.");
	}
	
	public static void printMissingArgumentsMessage() {
		IO.writeString("This command requires arguments.");
	}
	
	public static void printAmbiguousSelectionMessage() {
		IO.writeString("Provided input is ambiguous.");
	}
	
	public static void printFailedToCreateDirectoryMessage(String directory) {
		IO.writeString("Failed to create the '" + directory + "' directory.");
	}

}
