package com.znet.logging.demo.todos.models;

import java.util.List;

public class AppUser {

	private User user;
	private List<Task> tasks;
	private List<Friend> friends;

	public AppUser() {
		super();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public List<Friend> getFriends() {
		return friends;
	}

	public void setFriends(List<Friend> friends) {
		this.friends = friends;
	}
}
