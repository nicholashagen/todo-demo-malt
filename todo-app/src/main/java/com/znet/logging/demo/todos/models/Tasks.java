package com.znet.logging.demo.todos.models;

import java.util.List;

public class Tasks {

	private List<Task> items;

	public Tasks() {
		super();
	}

	public Tasks(List<Task> tasks) {
		this.items = tasks;
	}

	public List<Task> getItems() { return this.items; }

	public void setItems(List<Task> items) { this.items = items; }
}
