package io.javakata.unit.service.problem;

import static io.javakata.fixtrue.FixtureUtil.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import io.javakata.common.error.ErrorType;
import io.javakata.common.error.JavaKataException;
import io.javakata.controller.problem.request.CreateProblemRequest;
import io.javakata.repository.problem.Problem;
import io.javakata.repository.problem.ProblemCommand;
import io.javakata.repository.problem.category.ProblemCategory;
import io.javakata.repository.problem.category.ProblemCategoryQuery;
import io.javakata.service.problem.ProblemService;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 27.
 */
@ExtendWith({MockitoExtension.class})
public class ProblemServiceUnitTest {

	@InjectMocks
	private ProblemService problemService;

	@Mock
	private ProblemCategoryQuery problemCategoryQuery;

	@Mock
	private ProblemCommand problemCommand;

	@Nested
	@DisplayName("문제 생성 단위 테스트")
	class CreateProblemTest {

		ProblemCategory category;
		CreateProblemRequest request;
		Problem problem;

		@BeforeEach
		void setup() {
			category = defaultProblemCategory();
			request = defaultCreateProblemRequest();
			problem = Problem.withCreateRequestAndCategory(request, category);
		}

		@Test
		@DisplayName("성공")
		void success() {
			when(problemCategoryQuery.findById(anyLong())).thenReturn(Optional.of(category));
			when(problemCommand.save(any(Problem.class))).thenReturn(problem);

			Problem createdProblem = problemService.createProblem(request);

			assertThat(createdProblem).isNotNull();
		}

		@Test
		@DisplayName("실패 - 잘못된 category id ")
		void fail_when_category_name_duplicated() {
			given(problemCategoryQuery.findById(anyLong()))
				.willThrow(new JavaKataException(ErrorType.VALIDATION_ERROR));

			assertThatThrownBy(() -> problemService.createProblem(request))
				.isInstanceOf(JavaKataException.class)
				.hasMessageContaining(ErrorType.VALIDATION_ERROR.getMessage());
		}
	}

	@Nested
	@DisplayName("문제 수정 단위 테스트")
	class UpdateProblemTest {
		// 테스트케이스3개, 카테고리 1개, 문제 데이터 1개
		// ProblemCategory
	}
}
