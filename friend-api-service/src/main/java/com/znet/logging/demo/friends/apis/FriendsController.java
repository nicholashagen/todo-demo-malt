package com.znet.logging.demo.friends.apis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.znet.logging.demo.friends.models.Friends;
import com.znet.logging.demo.friends.services.FriendService;

@RestController
public class FriendsController {

	@Autowired
	private FriendService friendService;

	@RequestMapping("/friends/{userId}")
	public Friends getFriends(@PathVariable("userId") int userId) throws Exception {
		return friendService.getFriends(userId);
	}
}
