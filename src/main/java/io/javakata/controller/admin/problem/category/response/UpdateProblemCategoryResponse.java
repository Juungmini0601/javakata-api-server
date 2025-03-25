package io.javakata.controller.admin.problem.category.response;

import java.time.LocalDateTime;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 25.
 */
public record UpdateProblemCategoryResponse(
	Long id,
	String categoryName,
	LocalDateTime createdAt,
	LocalDateTime updatedAt
) {
}
