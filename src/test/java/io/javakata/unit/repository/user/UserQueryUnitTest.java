package io.javakata.unit.repository.user;

import static io.javakata.fixtrue.FixtureUtil.*;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import io.javakata.repository.user.User;
import io.javakata.repository.user.UserQuery;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 23.
 */
@DataJpaTest
@Import(UserQuery.class)
public class UserQueryUnitTest {
	@Autowired
	private TestEntityManager em;

	@Autowired
	private UserQuery userQuery;

	@Nested
	@DisplayName("existsByEmail 단위 테스트")
	class ExistsByEmailTest {

		User user;

		@BeforeEach
		void setUp() {
			user = defaultUserWithOutId();
		}

		@Test
		@DisplayName("유저가 존재하면 true를 반환한다.")
		void when_user_exists_then_return_true() {
			em.persist(user);
			String email = user.getEmail();

			boolean exists = userQuery.existsByEmail(email);

			assertThat(exists).isTrue();
		}

		@Test
		@DisplayName("유저가 존재하지 않으면 false를 반환한다.")
		void when_user_not_exists_then_return_false() {
			String email = user.getEmail();

			boolean exists = userQuery.existsByEmail(email);

			assertThat(exists).isFalse();
		}
	}
}
