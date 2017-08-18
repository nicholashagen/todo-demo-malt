package com.znet.logging.demo.auth.apis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.znet.logging.demo.auth.models.User;
import com.znet.logging.demo.auth.services.AuthService;

@RestController
public class AuthController {

	@Autowired
	private AuthService authService;

	@RequestMapping("/login/{username}")
	public User login(@PathVariable("username") String username, @RequestParam("password") String password)
		throws Exception {

		return authService.authenticate(username, password);
	}

	@RequestMapping("/users/{username}")
	public User getUser(@PathVariable("username") String username) throws Exception {
		return authService.getUser(username);
	}
}
