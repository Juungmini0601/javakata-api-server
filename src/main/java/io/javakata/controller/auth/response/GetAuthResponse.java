package io.javakata.controller.auth.response;

import java.time.LocalDateTime;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 24.
 */
public record GetAuthResponse(
	Long id,
	String email,
	String nickname,
	String role,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
}
