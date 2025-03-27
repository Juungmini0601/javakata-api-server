package io.javakata.controller.problem.response;

import java.util.List;

import io.javakata.controller.problem.testcase.response.UpdateTestCaseResponse;
import io.javakata.repository.problem.Level;
import io.javakata.repository.problem.Problem;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 27.
 */
@Builder
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetProblemResponse {
	private Long id;
	private String title;
	private Level level;
	private String description;
	private String constraints;
	private String input;
	private String expectedOutput;
	private String baseCode;
	private Long categoryId;
	private List<UpdateTestCaseResponse> testCases;

	public static GetProblemResponse from(Problem problem) {
		return GetProblemResponse.builder()
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
					.map(UpdateTestCaseResponse::from)
					.toList()
			)
			.build();
	}
}
