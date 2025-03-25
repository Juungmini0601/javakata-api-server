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

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 25.
 */
@DataJpaTest
@Import({ProblemCategoryCommand.class, JpaConfig.class})
public class ProblemCategoryCommandUnitTest {

	@Autowired
	ProblemCategoryCommand problemCategoryCommand;

	@Nested
	@DisplayName("save 단위 테스트")
	class SaveTest {
		ProblemCategory problemCategory;

		@BeforeEach
		void setup() {
			problemCategory = defaultProblemCategoryWithoutId();
		}

		@Test
		@DisplayName("성공")
		void success() {
			ProblemCategory savedCategory = problemCategoryCommand.save(problemCategory);

			assertThat(savedCategory.getId()).isNotNull();
		}
	}
}
