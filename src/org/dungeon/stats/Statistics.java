package org.dungeon.stats;

import java.io.Serializable;
import java.util.SortedSet;
import java.util.TreeSet;

import org.dungeon.commands.IssuedCommand;
import org.dungeon.util.Table;

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
	
	public void printStatistics() {
		Table statistics = new Table("Property", "Value");
		insertCommandStatistics(statistics);
		statistics.insertSeparator();
		insertWorldStatistics(statistics);
		statistics.print();
	}
	
	private void insertCommandStatistics(Table statistics) {
		int commandCount = commandStatistics.getCommandCount();
		int chars = commandStatistics.getChars();
		int words = commandStatistics.getWords();
		statistics.insertRow("Commands issued", String.valueOf(commandCount));
		statistics.insertRow("Characters entered", String.valueOf(chars));
		statistics.insertRow("Average characters per command", String.format("%.2f", (double) chars / commandCount));
		statistics.insertRow("Words entered", String.valueOf(words));
		statistics.insertRow("Average words per command", String.format("%.2f", (double) words / commandCount));
	}
	
	private void insertWorldStatistics(Table statistics) {
		statistics.insertRow("Created Locations", String.valueOf(worldStatistics.getLocationCount()));
		SortedSet<String> locationNames = new TreeSet<String>(worldStatistics.getLocationCounter().keySet());
		for (String name : locationNames) {
			statistics.insertRow("  " + name, String.valueOf(worldStatistics.getLocationCounter().getCounter(name)));
		}
		statistics.insertSeparator();
		statistics.insertRow("Spawned Creatures", String.valueOf(worldStatistics.getSpawnCount()));
		SortedSet<String> spawnNames = new TreeSet<String>(worldStatistics.getSpawnCounter().keySet());
		for (String name : spawnNames) {
			statistics.insertRow("  " + name, String.valueOf(worldStatistics.getSpawnCounter().getCounter(name)));
		}
	}
	
}
