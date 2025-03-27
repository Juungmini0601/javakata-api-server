package io.javakata.repository.problem;

import com.fasterxml.jackson.annotation.JsonBackReference;

import io.javakata.controller.problem.testcase.request.CreateTestCaseRequest;
import io.javakata.controller.problem.testcase.request.UpdateTestCaseRequest;
import io.javakata.repository.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 23.
 */
@ToString
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "test_cases")
public class TestCase extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String input;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String expectedOutput;

	@Column(nullable = false)
	private boolean isPublic;

	@ManyToOne
	@JoinColumn(name = "problem_id", nullable = false)
	@JsonBackReference // testcase -> problem 직렬화 X TODO 어차피 DTO로 변환되면서 바뀔 가능성 있음
	private Problem problem;

	public void update(UpdateTestCaseRequest request) {
		this.input = request.getInput();
		this.expectedOutput = request.getExpectedOutput();
		this.isPublic = request.isPublic();
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
	}

	public static TestCase withCreateRequest(CreateTestCaseRequest request) {
		return builder()
			.input(request.getInput())
			.expectedOutput(request.getExpectedOutput())
			.isPublic(request.isPublic())
			.build();
	}

	public static TestCase withUpdateRequest(UpdateTestCaseRequest request) {
		return TestCase.builder()
			.id(request.getId())
			.input(request.getInput())
			.expectedOutput(request.getExpectedOutput())
			.isPublic(request.isPublic())
			.build();
	}
}
