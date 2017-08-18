package com.znet.logging.demo.friends.models;

public class Friend {

	private int userId;
	private int friendId;

	public Friend(int userId, int friendId) {
		this.userId = userId;
		this.friendId = friendId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getFriendId() {
		return friendId;
	}

	public void setFriendId(int friendId) {
		this.friendId = friendId;
	}
}
