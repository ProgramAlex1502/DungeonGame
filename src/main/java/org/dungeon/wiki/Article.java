package main.java.org.dungeon.wiki;

final class Article {
	
	public final String title;
	public final String content;
	
	public Article(String title, String content) {
		this.title = title;
		this.content = content;
	}
	
	@Override
	public String toString() {
		return title + "\n\n" + content;
	}

}
