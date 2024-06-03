package com.example.demo.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ExampleController {

	@GetMapping("/example")
	public Void example(@RequestBody ExampleRequest exampleRequest) {
		throw new ExampleException("예시 에러");
	}
}
