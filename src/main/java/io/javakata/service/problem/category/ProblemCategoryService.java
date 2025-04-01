package io.javakata.service.problem.category;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.javakata.common.error.ErrorType;
import io.javakata.common.error.JavaKataException;
import io.javakata.controller.admin.problem.category.request.CreateProblemCategoryRequest;
import io.javakata.repository.problem.category.ProblemCategory;
import io.javakata.repository.problem.category.ProblemCategoryCommand;
import io.javakata.repository.problem.category.ProblemCategoryQuery;
import lombok.RequiredArgsConstructor;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 25.
 */
@Service
@RequiredArgsConstructor
public class ProblemCategoryService {
	private final ProblemCategoryCommand problemCategoryCommand;
	private final ProblemCategoryQuery problemCategoryQuery;

	@Transactional
	public ProblemCategory createCategory(CreateProblemCategoryRequest request) {
		final String categoryName = request.categoryName();

		if (problemCategoryQuery.existsByName(categoryName)) {
			throw new JavaKataException(ErrorType.CONFLICT_ERROR, "duplicated category name:" + categoryName);
		}

		ProblemCategory problemCategory = ProblemCategory.ofName(categoryName);

		return problemCategoryCommand.save(problemCategory);
	}
}
