package com.example.demo.controller.v1.request;

import com.example.demo.domain.ExampleData;

public record ExampleRequestDto(String data) {
	public ExampleData toExampleData() {
		return new ExampleData(data, data);
	}
}
