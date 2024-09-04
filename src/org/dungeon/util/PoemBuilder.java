package org.dungeon.util;

public class PoemBuilder {
	
	public String title;
	private String author;
	private String content;
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public void setAuthor(String author) {
		this.author = author;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public boolean isComplete() {
		return (title != null && author != null && content != null);
	}
	
	public Poem createPoem() {
		return new Poem(title, author, content);
	}

}
