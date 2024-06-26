package main.java.org.dungeon.stats;

import java.io.Serializable;
import java.util.SortedSet;
import java.util.TreeSet;

import main.java.org.dungeon.game.IssuedCommand;
import main.java.org.dungeon.io.IO;
import main.java.org.dungeon.util.CounterMap;

public final class Statistics implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private final WorldStatistics worldStatistics = new WorldStatistics();
	private final ExplorationStatistics explorationStatistics = new ExplorationStatistics();
	private final BattleStatistics battleStatistics = new BattleStatistics();
	private final CommandStatistics commandStatistics = new CommandStatistics();
	
	public WorldStatistics getWorldStatistics() {
		return worldStatistics;
	}

	public ExplorationStatistics getExplorationStatistics() {
		return explorationStatistics;
	}
	
	public BattleStatistics getBattleStatistics() {
		return battleStatistics;
	}
	
	public void addCommand(IssuedCommand issuedCommand) {
		commandStatistics.addCommand(issuedCommand);
	}
	
	public void printAllStatistics() {
		printCommandStatistics();
		printWorldStatistics();
	}
	
	private void printCommandStatistics() {
		int commandCount = commandStatistics.getCommandCount();
		int chars = commandStatistics.getChars();
		int words = commandStatistics.getWords();
		IO.writeKeyValueString("Commands issued", String.valueOf(commandCount));
		IO.writeKeyValueString("Characters entered", String.valueOf(chars));
		IO.writeKeyValueString("Average characters per command", String.format("%.2f", (double) chars / commandCount));
		IO.writeKeyValueString("Words entered", String.valueOf(words));
		IO.writeKeyValueString("Average words per command", String.format("%.2f", (double) words / commandCount));
	}
	
	private void printWorldStatistics() {
		IO.writeKeyValueString("Created Locations", String.valueOf(worldStatistics.getLocationCount()));
		IO.writeKeyValueString("Spawned Creatures", String.valueOf(worldStatistics.getCreatureCount()));
	}
	
	public void printSpawnStatistics() {
		CounterMap<String> spawnCounter = worldStatistics.getSpawnCounters();
		SortedSet<String> sortedSet = new TreeSet<String>(spawnCounter.keySet());
		for (String string : sortedSet) {
			IO.writeKeyValueString(string, String.valueOf(spawnCounter.getCounter(string)));
		}
	}

}
