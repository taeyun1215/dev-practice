package com.example.demo.core.api.controller.v1.request;

import com.example.demo.core.api.domain.ExampleData;

public record ExampleRequestDto(String data) {
	public ExampleData toExampleData() {
		return new ExampleData(data, data);
	}
}