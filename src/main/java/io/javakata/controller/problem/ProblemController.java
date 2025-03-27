package io.javakata.controller.problem;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.javakata.common.response.ApiResponse;
import io.javakata.controller.problem.response.GetProblemResponse;
import io.javakata.repository.problem.Problem;
import io.javakata.service.problem.ProblemService;
import lombok.RequiredArgsConstructor;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 27.
 */
@RestController
@RequiredArgsConstructor
public class ProblemController {

	private final ProblemService problemService;

	@GetMapping("/api/v1/problems/{problemId}")
	public ApiResponse<GetProblemResponse> fetchProblem(
		@PathVariable Long problemId
	) {
		Problem problem = problemService.findById(problemId);

		return ApiResponse.success(GetProblemResponse.from(problem));
	}
}
