package io.javakata.controller.problem.category;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import io.javakata.common.response.ApiResponse;
import io.javakata.controller.admin.problem.category.ProblemCategoryMapper;
import io.javakata.controller.problem.category.response.GetProblemCategoryResponse;
import io.javakata.repository.problem.category.ProblemCategory;
import io.javakata.service.problem.category.ProblemCategoryService;
import lombok.RequiredArgsConstructor;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 25.
 */
@RestController
@RequiredArgsConstructor
public class ProblemCategoryController {

	private final ProblemCategoryService problemCategoryService;

	@GetMapping("/api/v1/problems/categories")
	public ApiResponse<List<GetProblemCategoryResponse>> getProblemCategories() {
		List<ProblemCategory> problemCategories = problemCategoryService.findAll();
		List<GetProblemCategoryResponse> response = problemCategories.stream()
			.map(ProblemCategoryMapper.INSTANCE::toGetProblemCategoryResponse)
			.toList();

		return ApiResponse.success(response);
	}
}
