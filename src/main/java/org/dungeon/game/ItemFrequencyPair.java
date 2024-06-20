package main.java.org.dungeon.game;

public class ItemFrequencyPair {
	
	private final Pair<String, Double> pair;
	
	public ItemFrequencyPair(String id, double frequency) {
		this.pair = new Pair<String, Double>(id, frequency);
	}
	
	public String getId() {
		return pair.a;
	}
	
	public double getFrequency() {
		return pair.b;
	}

}
