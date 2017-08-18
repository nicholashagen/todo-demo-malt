package com.znet.logging.demo.tasks.models;

import java.util.Date;

public class Task {

	private int id;
	private int createdBy;
	private Date createdOn;
	private boolean completed;
	private String text;

	public Task(int id, int createdBy, Date createdOn, boolean completed, String text) {
		this.id = id;
		this.createdBy = createdBy;
		this.createdOn = createdOn;
		this.completed = completed;
		this.text = text;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
}
