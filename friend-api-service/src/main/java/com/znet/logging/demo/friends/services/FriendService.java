package com.znet.logging.demo.friends.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.codahale.metrics.MetricRegistry;
import com.znet.logging.demo.friends.models.Friend;
import com.znet.logging.demo.friends.models.Friends;

@Service
public class FriendService {

	private static final Logger LOG = LoggerFactory.getLogger(FriendService.class);

	@Autowired
    private JdbcTemplate jdbcTemplate;

	@Autowired
	private MetricRegistry registry;

	@NewSpan(name="getFriends")
	public Friends getFriends(int userId) throws Exception {

		LOG.info("Get friends for user {}", userId);

		List<Friend> friends = registry.timer("timer.friends.getfriends").time(() ->
			jdbcTemplate.query(
				"SELECT user_id, friend_id FROM friend WHERE user_id = ?",
				new Object[] { userId },
	            (rs, rowNum) -> new Friend(rs.getInt("user_id"), rs.getInt("friend_id"))
	        )
		);

		LOG.info("Returning {} friends for user {}", friends.size(), userId);
		return new Friends(friends);
	}
}
