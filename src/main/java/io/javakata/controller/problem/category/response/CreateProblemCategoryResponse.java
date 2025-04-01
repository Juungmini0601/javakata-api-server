package io.javakata.controller.problem.category.response;

import java.time.LocalDateTime;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 25.
 */
public record CreateProblemCategoryResponse(
	Long id,
	String categoryName,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
}
