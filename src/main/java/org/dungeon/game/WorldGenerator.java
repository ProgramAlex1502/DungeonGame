package main.java.org.dungeon.game;

public class WorldGenerator {
	
	//TODO: finish WorldGenerator class
	
	private final World world;
	
	private int chunkSide;
	
	private static final int DEF_CHUNK_SIDE = 5;
	
	public WorldGenerator(World world) {
		this(world, DEF_CHUNK_SIDE);
	}
	
	public WorldGenerator(World world, int chunkSide) {
		this.world = world;
		this.chunkSide = chunkSide;
	}

	public void expand(Point point) {
		//TODO: complete "world.expand(Point point)" method
		
	}

}
