package io.javakata.controller.problem.testcase.response;

import io.javakata.repository.problem.TestCase;
import lombok.Builder;

@Builder
public record CreateTestCaseResponse(
	Long id,
	String input,
	String expectedOutput,
	boolean isPublic
) {
	public static CreateTestCaseResponse from(TestCase testCase) {
		return CreateTestCaseResponse.builder()
			.id(testCase.getId())
			.input(testCase.getInput())
			.expectedOutput(testCase.getExpectedOutput())
			.isPublic(testCase.isPublic())
			.build();
	}
}
