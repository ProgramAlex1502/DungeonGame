package main.java.org.dungeon.util;

public class Poem {
	
	private final String title;
	private final String author;
	private final String content;
	
	public Poem(String title, String author, String content) {
		this.title = title;
		this.author = author;
		this.content = content;
	}
	
	@Override
	public String toString() {
		return title + "\n\n" + content + "\n\n" + author;
	}

}
