package main.java.org.dungeon.game;

final class LocationPreset extends Preset {
	
	//TODO: finish LocationPreset class
	
	private String name;
	private double lightPermittivity;
	
	LocationPreset(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public double getLightPermittivity() {
		return lightPermittivity;
	}

}
