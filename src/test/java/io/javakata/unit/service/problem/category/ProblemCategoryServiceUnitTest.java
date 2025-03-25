package io.javakata.unit.service.problem.category;

import static io.javakata.fixtrue.FixtureUtil.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

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
import io.javakata.controller.admin.problem.category.request.CreateProblemCategoryRequest;
import io.javakata.repository.problem.category.ProblemCategory;
import io.javakata.repository.problem.category.ProblemCategoryCommand;
import io.javakata.repository.problem.category.ProblemCategoryQuery;
import io.javakata.service.problem.category.ProblemCategoryService;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 25.
 */
@ExtendWith(MockitoExtension.class)
public class ProblemCategoryServiceUnitTest {
	@InjectMocks
	private ProblemCategoryService problemCategoryService;

	@Mock
	private ProblemCategoryCommand problemCategoryCommand;

	@Mock
	private ProblemCategoryQuery problemCategoryQuery;

	@Nested
	@DisplayName("카테고리 생성 단위 테스트")
	class CreateProblemCategoryTest {

		CreateProblemCategoryRequest request;
		ProblemCategory category;

		@BeforeEach
		void setup() {
			request = defaultCreateProblemCategoryRequest();
			category = problemCategoryFromCreateRequest(request);
		}

		@Test
		@DisplayName("성공")
		void success() {
			when(problemCategoryQuery.existsByName(anyString())).thenReturn(false);
			when(problemCategoryCommand.save(any(ProblemCategory.class)))
				.thenReturn(category);

			ProblemCategory createdCategory = problemCategoryService.createCategory(request);

			assertThat(createdCategory.getName()).isEqualTo(request.categoryName());
		}

		@Test
		@DisplayName("실패 - 중복된 카테고리 이름")
		void fail_when_category_name_duplicated() {
			when(problemCategoryQuery.existsByName(anyString())).thenReturn(true);

			assertThatThrownBy(() -> problemCategoryService.createCategory(request))
				.isInstanceOf(JavaKataException.class)
				.hasMessageContaining(ErrorType.CONFLICT_ERROR.getMessage());

		}
	}
}
