package com.znet.logging.demo.tasks.apis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.znet.logging.demo.tasks.models.Task;
import com.znet.logging.demo.tasks.models.Tasks;
import com.znet.logging.demo.tasks.services.TaskService;

@RestController
public class TaskController {

	@Autowired
	private TaskService taskService;

	@RequestMapping("/tasks/{userId}")
	public Tasks getTasks(@PathVariable("userId") int userId) throws Exception {
		return taskService.getTasks(userId);
	}

	@RequestMapping("/tasks/{userId}/{id}")
	public Task getTasks(@PathVariable("userId") int userId, @PathVariable("id") int id) {
		return taskService.getTask(userId, id);
	}
}
