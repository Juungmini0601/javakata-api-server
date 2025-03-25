package io.javakata.unit.repository.user;

import static io.javakata.fixtrue.FixtureUtil.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import io.javakata.common.config.JpaConfig;
import io.javakata.repository.user.User;
import io.javakata.repository.user.UserCommand;
import io.javakata.repository.user.UserQuery;
import jakarta.persistence.EntityManager;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 23.
 */
@DataJpaTest
@Import({UserCommand.class, UserQuery.class, JpaConfig.class})
public class UserCommandUnitTest {

	@Autowired
	EntityManager em;

	@Autowired
	private UserQuery userQuery;

	@Autowired
	private UserCommand userCommand;

	@Nested
	@DisplayName("save 단위 테스트")
	class SaveTest {
		User user;

		@BeforeEach
		void setUp() {
			user = defaultUserWithOutId();
		}

		@Test
		@DisplayName("성공")
		void success() {
			User savedUser = userCommand.save(user);

			assertThat(savedUser.getId()).isNotNull();
		}
	}

	@Nested
	@DisplayName("deleteByEmail 단위 테스트")
	class DeleteTest {
		User user;

		@BeforeEach
		void setUp() {
			user = defaultUserWithOutId();
		}

		@Test
		@DisplayName("성공")
		void success() {
			em.persist(user);
			em.flush();

			userCommand.delete(user.getEmail());
			Optional<User> foundUser = userQuery.findByEmail(user.getEmail());

			assertThat(foundUser).isNotPresent();
		}
	}
}
