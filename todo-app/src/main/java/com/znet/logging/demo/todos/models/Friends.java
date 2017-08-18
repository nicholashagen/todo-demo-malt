package com.znet.logging.demo.todos.models;

import java.util.List;

public class Friends {

	private List<Friend> items;

	public Friends() {
		super();
	}

	public Friends(List<Friend> friends) {
		this.items = friends;
	}

	public List<Friend> getItems() { return this.items; }

	public void setItems(List<Friend> items) { this.items = items; }
}
