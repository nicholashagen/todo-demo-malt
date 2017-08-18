package com.znet.logging.demo.tasks.models;

import java.util.List;

public class Tasks {

	private List<Task> items;

	public Tasks(List<Task> tasks) {
		this.items = tasks;
	}

	public List<Task> getItems() { return this.items; }

	public void setItems(List<Task> items) { this.items = items; }
}
