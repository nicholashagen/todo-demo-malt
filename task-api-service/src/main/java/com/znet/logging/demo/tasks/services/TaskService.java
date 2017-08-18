package com.znet.logging.demo.tasks.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.codahale.metrics.MetricRegistry;
import com.znet.logging.demo.tasks.models.Task;
import com.znet.logging.demo.tasks.models.Tasks;

@Service
public class TaskService {

	private static final Logger LOG = LoggerFactory.getLogger(TaskService.class);

	@Autowired
    private JdbcTemplate jdbcTemplate;

	@Autowired
	private MetricRegistry registry;

	@NewSpan(name="getTasks")
	public Tasks getTasks(int userId) throws Exception {

		LOG.info("Get list of user {} tasks", userId);

		List<Task> tasks = registry.timer("timer.tasks.gettasks").time(() ->
			jdbcTemplate.query(
				"SELECT id, created_by, created_on, completed, text FROM task WHERE created_by = ? ORDER BY created_on",
				new Object[] { userId },
	            (rs, rowNum) -> new Task(
	            	rs.getInt("id"), rs.getInt("created_by"),
	            	rs.getDate("created_on"), rs.getBoolean("completed"),
	            	rs.getString("text")
	            )
	        )
		);

		LOG.info("Returning {} tasks for user {}", tasks.size(), userId);
		return new Tasks(tasks);
	}

	@NewSpan(name="getTask")
	public Task getTask(int userId, int id) {

		LOG.info("Get task {} for user {}", id, userId);

		List<Task> tasks = jdbcTemplate.query(
			"SELECT id, created_by, created_on, completed, text FROM task WHERE id = ? AND created_by = ?",
			new Object[] { id, userId },
            (rs, rowNum) -> new Task(
            	rs.getInt("id"), rs.getInt("created_by"),
            	rs.getDate("created_on"), rs.getBoolean("completed"),
            	rs.getString("text")
            )
        );

		if (tasks.size() != 1) {
			LOG.info("No task found for task {} for user {}", id, userId);
			return null;
		}

		LOG.info("Returning task {} for user {}", id, userId);
		return tasks.get(0);
	}
}
