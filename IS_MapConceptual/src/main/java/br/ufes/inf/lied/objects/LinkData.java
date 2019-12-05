package br.ufes.inf.lied.objects;

import java.util.UUID;

public class LinkData {

	private UUID from;
	private UUID to;
	private String category;
	private String color;

	public LinkData() {
		super();
		// TODO Auto-generated constructor stub
	}

	public LinkData(UUID from, UUID to, String category, String color) {
		super();
		this.from = from;
		this.to = to;
		this.category = category;
		this.color = color;
	}

	public UUID getFrom() {
		return from;
	}

	public void setFrom(UUID from) {
		this.from = from;
	}

	public UUID getTo() {
		return to;
	}

	public void setTo(UUID to) {
		this.to = to;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
}