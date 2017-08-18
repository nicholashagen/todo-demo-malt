package com.znet.logging.demo.auth.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.codahale.metrics.MetricRegistry;
import com.znet.logging.demo.auth.models.User;

@Service
public class AuthService {

	private static final Logger LOG = LoggerFactory.getLogger(AuthService.class);

	@Autowired
    private JdbcTemplate jdbcTemplate;

	@Autowired
	private MetricRegistry registry;

	@NewSpan(name="authenticate")
	public User authenticate(String username, String password) throws Exception {

		LOG.info("Authenticating user {}", username);

		List<User> users = registry.timer("timer.auth.logins").time(() ->
			jdbcTemplate.query(
				"SELECT id, username FROM user WHERE username = ? AND password = ?",
				new Object[] { username, password },
	            (rs, rowNum) -> new User(rs.getInt("id"), rs.getString("username"))
	        )
		);

		if (users.size() != 1) {
			LOG.info("Unable to authenticate user {}", username);
			throw new IllegalStateException("no valid user found");
		}

		LOG.info("Authenticated user {}", username);
		return users.get(0);
	}

	@NewSpan(name="getUser")
	public User getUser(String username) throws Exception {

		LOG.info("Authenticating user {}", username);

		List<User> users = registry.timer("timer.auth.getuser").time(() -> {
			if ("john".equals(username)) { Thread.sleep(3000); }
			if ("error".equals(username)) { Thread.sleep(500); throw new IllegalStateException("failed user: " + username); }
			return jdbcTemplate.query(
				"SELECT id, username FROM user WHERE username = ?",
				new Object[] { username },
	            (rs, rowNum) -> new User(rs.getInt("id"), rs.getString("username"))
	        );
		});

		if (users.size() != 1) {
			LOG.info("Unable to find user {}", username);
			throw new IllegalStateException("no valid user found");
		}

		LOG.info("Found user {}", username);
		return users.get(0);
	}
}
