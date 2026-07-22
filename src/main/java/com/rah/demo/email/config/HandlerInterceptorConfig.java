package com.rah.demo.email.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class HandlerInterceptorConfig implements HandlerInterceptor {

	@Value("${gateway.secret}")
	private String gatewaySecret;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String gatewaySecret = request.getHeader("X-GATEWAY-SECRET");

		if (this.gatewaySecret.equals(gatewaySecret)) {
			return true;
		}

		this.setErrorMessage(response);

		return false;
	}

	void setErrorMessage(HttpServletResponse response) throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);

		String jsonError = """
				{
				    "error": "Unauthorized",
				    "mensaje": "Petición no permitida.",
				    "status": 401
				}
				""";

		response.getWriter().write(jsonError);
	}

}
