package com.znet.logging.demo.todos.apis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.znet.logging.demo.todos.models.AppUser;
import com.znet.logging.demo.todos.services.TodoService;

@RestController
public class TodoController {

	@Autowired
	private TodoService todoService;

	@RequestMapping("/users/{username}")
	public AppUser getUser(@PathVariable("username") String username) throws Exception {
		return todoService.getUser(username);
	}
}
