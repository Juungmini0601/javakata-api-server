package io.javakata.controller.problem.testcase.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 27.
 */
@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateTestCaseRequest {

	@NotNull
	@NotBlank(message = "입력값은 필수입니다.")
	private String input;

	@NotNull
	@NotBlank(message = "기대 출력값은 필수입니다.")
	private String expectedOutput;

	private boolean isPublic;
}

