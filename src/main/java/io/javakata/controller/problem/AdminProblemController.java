package io.javakata.controller.problem;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.javakata.common.response.ApiResponse;
import io.javakata.controller.problem.request.CreateProblemRequest;
import io.javakata.controller.problem.request.UpdateProblemRequest;
import io.javakata.controller.problem.response.CreateProblemResponse;
import io.javakata.controller.problem.response.GetProblemResponse;
import io.javakata.controller.problem.response.UpdateProblemResponse;
import io.javakata.repository.problem.Problem;
import io.javakata.service.problem.ProblemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 27.
 */
@RestController
@RequiredArgsConstructor
public class AdminProblemController {

	private final ProblemService problemService;

	@GetMapping("/api/v1/admin/problems/{problemId}")
	public ApiResponse<GetProblemResponse> fetchProblem(
		@PathVariable Long problemId
	) {
		Problem problem = problemService.findById(problemId);

		return ApiResponse.success(GetProblemResponse.from(problem));
	}

	@PostMapping("/api/v1/admin/problems")
	public ApiResponse<CreateProblemResponse> createProblem(
		@Valid @RequestBody CreateProblemRequest request
	) {
		Problem problem = problemService.createProblem(request);

		return ApiResponse.success(CreateProblemResponse.from(problem));
	}

	@PutMapping("/api/v1/admin/problems/{problemId}")
	public ApiResponse<UpdateProblemResponse> updateProblem(
		@PathVariable Long problemId,
		@Valid @RequestBody UpdateProblemRequest request
	) {
		Problem problem = problemService.updateProblem(problemId, request);

		return ApiResponse.success(UpdateProblemResponse.from(problem));
	}

	@DeleteMapping("/api/v1/admin/problems/{problemId}")
	public ApiResponse<?> deleteProblem(
		@PathVariable Long problemId
	) {
		problemService.deleteProblem(problemId);

		return ApiResponse.success();
	}
}
