package io.javakata.controller.problem.testcase.response;

import io.javakata.repository.problem.TestCase;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTestCaseResponse {
	private Long id;
	private String input;
	private String expectedOutput;
	private boolean isPublic;

	public static UpdateTestCaseResponse from(TestCase testCase) {
		return UpdateTestCaseResponse.builder()
			.id(testCase.getId())
			.input(testCase.getInput())
			.expectedOutput(testCase.getExpectedOutput())
			.isPublic(testCase.isPublic())
			.build();
	}
}
