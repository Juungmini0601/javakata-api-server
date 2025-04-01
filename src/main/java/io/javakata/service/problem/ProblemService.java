package io.javakata.service.problem;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.javakata.common.error.ErrorType;
import io.javakata.common.error.JavaKataException;
import io.javakata.controller.problem.request.CreateProblemRequest;
import io.javakata.repository.problem.Problem;
import io.javakata.repository.problem.ProblemCommand;
import io.javakata.repository.problem.category.ProblemCategory;
import io.javakata.repository.problem.category.ProblemCategoryQuery;
import lombok.RequiredArgsConstructor;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 27.
 */
@Service
@RequiredArgsConstructor
public class ProblemService {

	private final ProblemCategoryQuery problemCategoryQuery;
	private final ProblemCommand problemCommand;

	@Transactional
	public Problem createProblem(CreateProblemRequest request) {
		ProblemCategory category = problemCategoryQuery.findById(request.getCategoryId())
			.orElseThrow(() ->
				new JavaKataException(ErrorType.VALIDATION_ERROR, "invalid category id: " + request.getCategoryId()));

		Problem problem = Problem.withCreateRequestAndCategory(request, category);

		return problemCommand.save(problem);
	}
}
