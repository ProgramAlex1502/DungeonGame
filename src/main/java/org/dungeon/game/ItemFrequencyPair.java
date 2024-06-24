package main.java.org.dungeon.game;

class ItemFrequencyPair {
	
	private final Pair<ID, Double> pair;
	
	public ItemFrequencyPair(ID id, double frequency) {
		this.pair = new Pair<ID, Double>(id, frequency);
	}
	
	public ID getId() {
		return pair.a;
	}
	
	public double getFrequency() {
		return pair.b;
	}

}
