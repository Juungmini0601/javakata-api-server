package io.javakata.unit.service.user;

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
import org.springframework.security.crypto.password.PasswordEncoder;

import io.javakata.common.error.ErrorType;
import io.javakata.common.error.JavaKataException;
import io.javakata.controller.user.request.CreateUserRequest;
import io.javakata.repository.user.User;
import io.javakata.repository.user.UserCommand;
import io.javakata.repository.user.UserQuery;
import io.javakata.service.user.UserService;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 23.
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceUnitTest {

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private UserCommand userCommand;

	@Mock
	private UserQuery userQuery;

	@InjectMocks
	private UserService userService;

	@Nested
	@DisplayName("회원 가입 단위 테스트")
	class RegisterTest {

		CreateUserRequest request;

		@BeforeEach
		void setUp() {
			request = defaultCreateUserRequest();
		}

		@Test
		@DisplayName("성공")
		void success() {
			String encryptedPassword = request.password() + "encrypted";
			User user = User.withRegisterInfo(request.email(), encryptedPassword, request.nickname());

			when(userQuery.existsByEmail(anyString())).thenReturn(false);
			when(passwordEncoder.encode(anyString())).thenReturn(encryptedPassword);
			when(userCommand.save(any(User.class))).thenReturn(user);

			User registeredUser = userService.register(request);

			assertThat(registeredUser.getEmail()).isEqualTo(request.email());
			assertThat(registeredUser.getPassword()).isEqualTo(encryptedPassword);
		}

		@Test
		@DisplayName("실패 - 이메일이 중복 되는 경우")
		void fail_when_email_is_duplicated() {
			when(userQuery.existsByEmail(anyString())).thenReturn(true);

			assertThatThrownBy(() -> userService.register(request))
				.isInstanceOf(JavaKataException.class)
				.hasMessage(ErrorType.CONFLICT_ERROR.getMessage());
		}
	}
}
