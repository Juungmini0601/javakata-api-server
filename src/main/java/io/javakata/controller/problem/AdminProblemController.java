package io.javakata.controller.problem;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.javakata.common.response.ApiResponse;
import io.javakata.controller.problem.request.CreateProblemRequest;
import io.javakata.controller.problem.response.CreateProblemResponse;
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

	@PostMapping("/api/v1/admin/problems")
	public ApiResponse<CreateProblemResponse> createProblem(
		@Valid @RequestBody CreateProblemRequest request
	) {
		Problem problem = problemService.createProblem(request);

		return ApiResponse.success(CreateProblemResponse.from(problem));
	}
}
