package main.java.org.dungeon.game;

public class Location {
	
	//TODO: finish Location class
	
	private World world;
	
	private final double lightPermittivity;
	
	public Location(LocationPreset preset, World world) {
		this.world = world;
		
		this.lightPermittivity = preset.getLightPermittivity();
	}
	
	double getLightPermittivity() {
		return lightPermittivity;
	}
	
	public double getLuminosity() {
		return getLightPermittivity() * getWorld().getPartOfDay().getLuminosity();
	}
	
	public World getWorld() {
		return world;
	}
}
