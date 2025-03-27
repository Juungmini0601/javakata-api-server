package io.javakata.service.problem;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.javakata.common.error.ErrorType;
import io.javakata.common.error.JavaKataException;
import io.javakata.controller.problem.request.CreateProblemRequest;
import io.javakata.controller.problem.request.UpdateProblemRequest;
import io.javakata.repository.problem.Problem;
import io.javakata.repository.problem.ProblemCommand;
import io.javakata.repository.problem.ProblemQuery;
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
	// TODO ProblemCategoryQuery는 ProblemQuery로 통합 하는게 좋아 보인다..
	private final ProblemCategoryQuery problemCategoryQuery;
	private final ProblemCommand problemCommand;
	private final ProblemQuery problemQuery;

	@Transactional(readOnly = true)
	public Problem findById(final Long id) {
		return problemQuery.findById(id)
			.orElseThrow(() -> new JavaKataException(ErrorType.VALIDATION_ERROR, "not found problem id: " + id));
	}

	@Transactional
	public Problem createProblem(CreateProblemRequest request) {
		ProblemCategory category = problemCategoryQuery.findById(request.getCategoryId())
			.orElseThrow(() ->
				new JavaKataException(ErrorType.VALIDATION_ERROR, "invalid category id: " + request.getCategoryId()));

		Problem problem = Problem.withCreateRequestAndCategory(request, category);

		return problemCommand.save(problem);
	}

	@Transactional
	public Problem updateProblem(final Long id, UpdateProblemRequest request) {
		ProblemCategory category = problemCategoryQuery.findById(request.getCategoryId())
			.orElseThrow(() ->
				new JavaKataException(ErrorType.VALIDATION_ERROR, "invalid category id: " + request.getCategoryId()));

		Problem problem = problemQuery.findById(id)
			.orElseThrow(() -> new JavaKataException(ErrorType.VALIDATION_ERROR, "not found problem id: " + id));

		problem.update(request, category);

		return problem;
	}

	@Transactional
	public void deleteProblem(final Long id) {
		problemCommand.deleteById(id);
	}
}
