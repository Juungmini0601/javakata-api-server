package io.javakata.controller.problem.response;

import java.util.List;

import io.javakata.controller.problem.testcase.response.CreateTestCaseResponse;
import io.javakata.repository.problem.Level;
import io.javakata.repository.problem.Problem;
import lombok.Builder;

@Builder
public record CreateProblemResponse(
	Long id,
	String title,
	Level level,
	String description,
	String constraints,
	String input,
	String expectedOutput,
	String baseCode,
	Long categoryId,
	List<CreateTestCaseResponse> testCases
) {

	public static CreateProblemResponse from(Problem problem) {
		return CreateProblemResponse.builder()
			.id(problem.getId())
			.title(problem.getTitle())
			.level(problem.getLevel())
			.description(problem.getDescription())
			.constraints(problem.getConstraints())
			.input(problem.getInput())
			.expectedOutput(problem.getExpectedOutput())
			.baseCode(problem.getBaseCode())
			.categoryId(problem.getCategory().getId())
			.testCases(
				problem.getTestCases().stream()
					.map(CreateTestCaseResponse::from)
					.toList()
			)
			.build();
	}

}
