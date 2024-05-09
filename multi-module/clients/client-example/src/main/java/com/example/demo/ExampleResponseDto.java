package com.example.demo;

import com.example.demo.model.ExampleClientResult;

record ExampleResponseDto(String exampleResponseValue) {
	ExampleClientResult toResult() {
		return new ExampleClientResult(exampleResponseValue);
	}
}
