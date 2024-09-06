package org.dungeon.wiki;

import org.dungeon.game.Name;
import org.dungeon.game.Selectable;

final class Article implements Selectable {
	
	private final Name title;
	private final String content;
	
	public Article(String title, String content) {
		this.title = Name.newInstance(title);
		this.content = content;
	}
	
	@Override
	public Name getName() {
		return title;
	}
	
	@Override
	public String toString() {
		return title + "\n\n" + content;
	}

}
