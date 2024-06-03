package com.example.demo.global;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Component
public class CachingRequestBodyFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		if (request instanceof ContentCachingRequestWrapper) {
			filterChain.doFilter(request, response);
		} else {
			ContentCachingRequestWrapper cachingWrapper = new ContentCachingRequestWrapper(request);
			filterChain.doFilter(cachingWrapper, response);
		}
	}
}
