package com.electroStore.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/request")
public class HomeController {
	@GetMapping()
	public String example() {
		return "Welcome to the electronic Store";
	}

}
