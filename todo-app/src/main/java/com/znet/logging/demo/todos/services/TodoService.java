package com.znet.logging.demo.todos.services;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.codahale.metrics.MetricRegistry;
import com.znet.logging.demo.todos.models.AppUser;
import com.znet.logging.demo.todos.models.Friends;
import com.znet.logging.demo.todos.models.Tasks;
import com.znet.logging.demo.todos.models.User;

@Service
public class TodoService {

	private static final Logger LOG = LoggerFactory.getLogger(TodoService.class);

	@Autowired
	private RestTemplate restClient;

	@Autowired
	private MetricRegistry registry;

	@NewSpan(name="getUser")
	public AppUser getUser(String username) throws Exception {

		if ("alert".equals(username)) {
			LOG.info("LOGIN FAIL");
		}

		return registry.timer("timer.todo.getUser").time(() -> {
			User user = fetchUser(username);
			Tasks tasks = fetchTasks(user.getId());
			Friends friends = fetchFriends(user.getId());

			AppUser appUser = new AppUser();
			appUser.setUser(user);
			appUser.setTasks(tasks.getItems());
			appUser.setFriends(friends.getItems());
			return appUser;
		});
	}

	protected User fetchUser(String username) throws Exception {
		LOG.info("Get user for username {}", username);
		return registry.timer("timer.todo.user").time(() ->
			restClient.getForEntity(
				URI.create("http://AUTH-API-SERVICE/users/" + username),
				User.class
			).getBody()
		);
	}

	protected Tasks fetchTasks(int userId) throws Exception {
		LOG.info("Get tasks for user {}", userId);
		return registry.timer("timer.todo.tasks").time(() ->
			restClient.getForEntity(
				URI.create("http://TASK-API-SERVICE/tasks/" + userId),
				Tasks.class
			).getBody()
		);
	}

	protected Friends fetchFriends(int userId) throws Exception {
		LOG.info("Get friends for user {}", userId);
		return registry.timer("timer.todo.friends").time(() ->
			restClient.getForEntity(
				URI.create("http://FRIEND-API-SERVICE/friends/" + userId),
				Friends.class
			).getBody()
		);
	}
}
