package com.cs628.helper;

public class News {

	@Override
	public String toString() {
		return "News [title=" + title + ", keyword=" + keyword
				+ ", highlights=" + highlights + "]";
	}
	
	private String title;
	private String keyword;
	private String highlights;
	public News(String title, String keyword, String highlights) {
		super();
		this.title = title;
		this.keyword = keyword;
		this.highlights = highlights;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getHighlights() {
		return highlights;
	}
	public void setHighlights(String highlights) {
		this.highlights = highlights;
	}
	
	
}
