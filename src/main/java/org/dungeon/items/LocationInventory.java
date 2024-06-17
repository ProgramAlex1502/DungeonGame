package main.java.org.dungeon.items;

public class LocationInventory extends BaseInventory {
	private static final long serialVersionUID = 1L;

	@Override
	public boolean addItem(Item item) {
		items.add(item);
		item.setOwner(null);
		return true;
	}

	@Override
	public void removeItem(Item item) {
		items.remove(item);
	}

}
