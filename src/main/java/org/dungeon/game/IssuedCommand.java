package main.java.org.dungeon.game;

import java.util.Arrays;

import main.java.org.dungeon.util.Utils;

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
	
	public boolean firstTokenEquals(String token) {
		return tokens[0].equalsIgnoreCase(token);
	}
	
	public boolean hasArguments() {
		return tokens.length > 1;
	}
	
	public String getFirstArgument() {
		return tokens[1];
	}
	
	public boolean firstArgumentEquals(String argument) {
		if (hasArguments()) {
			return tokens[1].equalsIgnoreCase(argument);
		} else {
			throw new IllegalArgumentException("this command does not have arguments.");
		}
	}
	
	public String[] getArguments() {
		return Arrays.copyOfRange(tokens, 1, tokens.length);
	}
	
	public int getTokenCount() {
		return tokens.length;
	}
	
}
