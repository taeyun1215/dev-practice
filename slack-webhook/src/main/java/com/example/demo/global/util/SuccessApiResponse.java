package com.example.demo.global.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SuccessApiResponse<T> extends ApiResponse {
	private T data;

	private SuccessApiResponse(int status, String message, T data) {
		super(status, message);
		this.data = data;
	}

	// 반환할 데이터가 없는 경우(메세지만)
	public static SuccessApiResponse<String> of(int status, String message) {
		return new SuccessApiResponse<>(status, message, null);
	}

	// 반환할 데이터가 있는 경우(데이터만)
	public static <T> SuccessApiResponse<T> of(int status, T data) {
		return new SuccessApiResponse<>(status, null, data);
	}

	// 반환할 데이터와 메세지가 있는 경우(메세지 + 데이터)
	public static <T> SuccessApiResponse<T> of(int status, String message, T data) {
		return new SuccessApiResponse<>(status, message, data);
	}
}