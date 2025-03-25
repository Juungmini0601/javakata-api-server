package io.javakata.unit.repository.problem.category;

import static io.javakata.fixtrue.FixtureUtil.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import io.javakata.common.config.JpaConfig;
import io.javakata.repository.problem.category.ProblemCategory;
import io.javakata.repository.problem.category.ProblemCategoryCommand;
import io.javakata.repository.problem.category.ProblemCategoryQuery;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 25.
 */
@DataJpaTest
@Import({ProblemCategoryQuery.class, ProblemCategoryCommand.class, JpaConfig.class})
public class ProblemCategoryQueryUnitTest {

	@Autowired
	ProblemCategoryQuery problemCategoryQuery;

	@Autowired
	ProblemCategoryCommand problemCategoryCommand;

	@Nested
	@DisplayName("existsByName 단위 테스트")
	class ExistsByNameTest {

		ProblemCategory problemCategory;

		@BeforeEach
		void setup() {
			problemCategory = defaultProblemCategoryWithoutId();
		}

		@Test
		@DisplayName("카테고리 이름이 존재하면 true를 반환한다.")
		void when_category_exists_then_return_true() {
			ProblemCategory savedCategory = problemCategoryCommand.save(problemCategory);

			assertThat(problemCategoryQuery.existsByName(savedCategory.getName())).isTrue();
		}

		@Test
		@DisplayName("카테고리 이름이 존재하지 않으면 False를 반환한다.")
		void when_category_not_exists_then_return_false() {
			assertThat(problemCategoryQuery.existsByName("categoryName")).isFalse();
		}
	}
}
