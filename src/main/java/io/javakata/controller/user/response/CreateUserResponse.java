package io.javakata.controller.user.response;

import java.time.LocalDateTime;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 23.
 */
public record CreateUserResponse(
	Long id,
	String email,
	String nickname,
	String role,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
}
