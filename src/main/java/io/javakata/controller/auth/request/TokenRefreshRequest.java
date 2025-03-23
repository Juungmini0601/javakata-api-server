package io.javakata.controller.auth.request;

import jakarta.validation.constraints.NotBlank;

public record TokenRefreshRequest(
	@NotBlank
	String accessToken
) {
}