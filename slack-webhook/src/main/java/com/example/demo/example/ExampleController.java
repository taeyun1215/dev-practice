package com.example.demo.example;

import com.example.demo.global.annotation.SlackNotification;
import com.example.demo.global.util.SuccessApiResponse;
import net.gpedro.integrations.slack.SlackApi;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class ExampleController {

	private final SlackApi slackApi;

	@GetMapping("/example")
	public void example(@RequestBody ExampleRequest exampleRequest) {
		throw new ExampleException("예시 에러");
	}
}
