package com.test;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.fasterxml.jackson.databind.JsonNode;

@org.springframework.web.bind.annotation.RestController
public class RestController {
	@CrossOrigin(origins = { "*" }, allowCredentials = "false")
	@PostMapping(path = "/{name}/{jsonNode}")
	public String createInstance(@PathVariable String name, @PathVariable JsonNode jsonNode) {
		// TODO Auto-generated method stub
		return"HI";
	}
}
