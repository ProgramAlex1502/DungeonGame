package org.dungeon.commands;

import java.util.Arrays;

import org.dungeon.io.DLogger;
import org.dungeon.util.Utils;

public final class IssuedCommand {
	
	private final String stringRepresentation;
	private final String[] tokens;
	
	public IssuedCommand(String source) {
		this.tokens = Utils.split(source);
		this.stringRepresentation = Utils.join(" ", tokens);
		
		if (tokens.length == 0) {
			throw new IllegalArgumentException("source must contain at least one token.");
		}
	}
	
	public String getStringRepresentation() {
		return stringRepresentation;
	}
	
	public String getFirstToken() {
		return tokens[0];
	}
	
	public boolean hasArguments() {
		return tokens.length > 1;
	}
	
	public String getFirstArgument() {
		return tokens[1];
	}
	
	public boolean firstArgumentEquals(String string) {
		if (hasArguments()) {
			return tokens[1].equalsIgnoreCase(string);
		} else {
			DLogger.warning("Called firstArgumentEquals on an IssuedCommand that does not have arguments.");
			return false;
		}
	}
	
	public String[] getArguments() {
		return Arrays.copyOfRange(tokens, 1, tokens.length);
	}
	
	public int getTokenCount() {
		return tokens.length;
	}

}
