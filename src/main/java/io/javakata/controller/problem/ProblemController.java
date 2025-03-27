package io.javakata.controller.problem;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.javakata.common.response.ApiResponse;
import io.javakata.controller.problem.response.GetProblemResponse;
import io.javakata.repository.problem.Problem;
import io.javakata.repository.problem.ProblemListSearchParam;
import io.javakata.repository.problem.ProblemWithCategory;
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

	@GetMapping("/api/v1/problems")
	public ApiResponse<Page<ProblemWithCategory>> fetchProblems(
		@PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
		@RequestParam(required = false) List<String> categories,
		@RequestParam(required = false) List<String> levels,
		@RequestParam(required = false) String keyword
	) {
		ProblemListSearchParam problemListSearchParam =
			new ProblemListSearchParam(pageable, keyword, categories, levels);

		Page<ProblemWithCategory> problems = problemService.fetchProblemList(problemListSearchParam);

		return ApiResponse.success(problems);
	}

	@GetMapping("/api/v1/problems/levels")
	public ApiResponse<List<String>> fetchLevels() {
		List<String> levels = problemService.getDistinctLevels();
		return ApiResponse.success(levels);
	}
}
