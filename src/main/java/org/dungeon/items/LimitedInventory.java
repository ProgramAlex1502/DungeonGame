package main.java.org.dungeon.items;

interface LimitedInventory {
	
	public int getLimit();
	
	public void setLimit(int limit);
	
	public boolean isFull();
	
}
