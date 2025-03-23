package io.javakata.controller.auth.response;

public record TokenRefreshResponse(
	String accessToken,
	String refreshToken
) {
}