package br.ufes.inf.lied.objects;

import java.util.UUID;

public class NodeData {

	private UUID key;
	private String text;
	private String category;
	private String loc;

	public NodeData() {
		super();
		// TODO Auto-generated constructor stub
	}

	public NodeData(UUID key, String text, String category, String loc) {
		super();
		this.key = key;
		this.text = text;
		this.category = category;
		this.loc = loc;
	}

	public UUID getKey() {
		return key;
	}

	public void setKey(UUID key) {
		this.key = key;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getLoc() {
		return loc;
	}

	public void setLoc(String loc) {
		this.loc = loc;
	}

}