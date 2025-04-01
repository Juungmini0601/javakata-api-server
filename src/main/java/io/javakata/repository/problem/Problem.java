package io.javakata.repository.problem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import io.javakata.common.error.ErrorType;
import io.javakata.common.error.JavaKataException;
import io.javakata.controller.problem.request.CreateProblemRequest;
import io.javakata.controller.problem.request.UpdateProblemRequest;
import io.javakata.controller.problem.testcase.request.UpdateTestCaseRequest;
import io.javakata.repository.BaseEntity;
import io.javakata.repository.problem.category.ProblemCategory;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 17.
 */
@ToString
@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "problems")
public class Problem extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Enumerated(EnumType.STRING)
	private Level level;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String description;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String constraints; // 입력 제한조건

	@Column(nullable = false, columnDefinition = "TEXT")
	private String input;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String expectedOutput; // 문제의 기대 출력값 설명

	@Column(nullable = false, columnDefinition = "TEXT")
	private String baseCode;

	@ManyToOne
	@JoinColumn(name = "category_id", nullable = false)
	private ProblemCategory category;

	// cascade -> problem 생성, 삭제시 testcase들도 같이 반영
	// orphanRemoval -> problem의 testcases에서 리스트에서 제거되면 디비에서 삭제
	@OneToMany(mappedBy = "problem", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference // problem -> testCase 직렬화 OK
	@ToString.Exclude
	private List<TestCase> testCases;

	public void update(UpdateProblemRequest request, ProblemCategory category) {
		title = request.getTitle();
		level = request.getLevel();
		description = request.getDescription();
		constraints = request.getConstraints();
		input = request.getInput();
		expectedOutput = request.getExpectedOutput();
		baseCode = request.getBaseCode();
		this.category = category;

		// TODO Null이어도 수정되게 해야 될 거 같은데 일단은 Null인 경우 없다고 가정
		Map<Long, UpdateTestCaseRequest> existingMap = request.getTestCases().stream()
			.collect(Collectors.toMap(UpdateTestCaseRequest::getId, tc -> tc));

		testCases.forEach(testCase -> {
			UpdateTestCaseRequest updateTestCaseRequest = existingMap.get(testCase.getId());
			if (updateTestCaseRequest == null) {
				throw new JavaKataException(ErrorType.VALIDATION_ERROR, "not found testCase Id:" + testCase.getId());
			}

			testCase.update(updateTestCaseRequest);
		});
	}

	// TODO 연관관계 편의 메서드 블로깅
	public void addTestCase(TestCase testCase) {
		testCases.add(testCase);
		testCase.setProblem(this);
	}

	public static Problem withCreateRequestAndCategory(CreateProblemRequest request, ProblemCategory category) {
		Problem problem = builder()
			.title(request.getTitle())
			.level(request.getLevel())
			.description(request.getDescription())
			.constraints(request.getConstraints())
			.input(request.getInput())
			.expectedOutput(request.getExpectedOutput())
			.baseCode(request.getBaseCode())
			.category(category)
			.testCases(new ArrayList<>())
			.build();

		request.getTestCases().forEach(testCaseRequest -> {
			TestCase testCase = TestCase.withCreateRequest(testCaseRequest);
			problem.addTestCase(testCase);
		});

		return problem;
	}
}
