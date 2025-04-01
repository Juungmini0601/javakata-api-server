package io.javakata.controller.admin.problem.category;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.javakata.common.response.ApiResponse;
import io.javakata.controller.admin.problem.category.request.CreateProblemCategoryRequest;
import io.javakata.controller.admin.problem.category.response.CreateProblemCategoryResponse;
import io.javakata.repository.problem.category.ProblemCategory;
import io.javakata.service.problem.category.ProblemCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 25.
 */
@RestController
@RequiredArgsConstructor
public class AdminProblemCategoryController {

	private final ProblemCategoryService problemCategoryService;

	@PostMapping("/api/v1/admin/problems/categories")
	public ApiResponse<CreateProblemCategoryResponse> createProblemCategory(
		@Valid @RequestBody CreateProblemCategoryRequest request
	) {
		ProblemCategory category = problemCategoryService.createCategory(request);

		return ApiResponse.success(ProblemCategoryMapper.INSTANCE.toCreateProblemCategoryResponse(category));
	}
}
