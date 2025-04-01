package io.javakata.controller.auth.response;

public record SinginResponse(
	String accessToken,
	String refreshToken
) {
}