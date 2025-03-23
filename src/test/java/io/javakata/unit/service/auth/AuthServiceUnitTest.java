package io.javakata.unit.service.auth;

import static io.javakata.fixtrue.FixtureUtil.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

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
import io.javakata.controller.auth.request.SigninRequest;
import io.javakata.repository.auth.Token;
import io.javakata.repository.auth.TokenClaim;
import io.javakata.repository.auth.TokenCommand;
import io.javakata.repository.auth.TokenQuery;
import io.javakata.repository.user.User;
import io.javakata.repository.user.UserQuery;
import io.javakata.service.auth.AuthService;
import io.javakata.service.auth.TokenService;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 23.
 */
@ExtendWith(MockitoExtension.class)
public class AuthServiceUnitTest {
	@InjectMocks
	private AuthService authService;

	@Mock
	private UserQuery userQuery;

	@Mock
	private TokenQuery tokenQuery;

	@Mock
	private TokenService tokenService;

	@Mock
	private TokenCommand tokenCommand;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Nested
	@DisplayName("토큰 발급 테스트")
	class GenerateTokenTest {
		SigninRequest request;
		User user;
		TokenClaim tokenClaim;
		Token token;

		@BeforeEach
		void setUp() {
			user = defaultUser();
			request = defaultSigninRequest();
			tokenClaim = defaultTokenClaim();
			token = defaultToken();
		}

		@Test
		@DisplayName("성공")
		void success() {
			when(userQuery.findByEmail(anyString()))
				.thenReturn(Optional.of(user));

			when(passwordEncoder.matches(anyString(), anyString()))
				.thenReturn(true);

			doNothing().when(tokenCommand).appendToken(any(Token.class));

			when(tokenService.generateToken(any(TokenClaim.class)))
				.thenReturn(token);

			Token generatedToken = authService.generateToken(request);

			assertThat(generatedToken.getAccessToken()).isNotNull();
			assertThat(generatedToken.getRefreshToken()).isNotNull();
		}

		@Test
		@DisplayName("실패 - 존재하지 않는 이메일")
		void fail_when_email_not_exists() {
			when(userQuery.findByEmail(anyString()))
				.thenReturn(Optional.empty());

			assertThatThrownBy(() -> authService.generateToken(request))
				.isInstanceOf(JavaKataException.class)
				.hasMessageContaining(ErrorType.AUTHENTICATION_ERROR.getMessage());
		}

		@Test
		@DisplayName("실패 - 비밀번호 불일치")
		void fail_when_password_not_match() {
			when(userQuery.findByEmail(anyString()))
				.thenReturn(Optional.of(user));

			when(passwordEncoder.matches(anyString(), anyString()))
				.thenReturn(false);

			assertThatThrownBy(() -> authService.generateToken(request))
				.isInstanceOf(JavaKataException.class)
				.hasMessageContaining(ErrorType.AUTHENTICATION_ERROR.getMessage());
		}
	}

	@Nested
	@DisplayName("토큰 재발급 테스트")
	class RefreshTokenTest {
		Token token;
		TokenClaim tokenClaim;

		@BeforeEach
		void setup() {
			token = defaultToken();
			tokenClaim = defaultTokenClaim();
		}

		@Test
		@DisplayName("성공")
		void success() {
			when(tokenQuery.getRefreshToken(anyString()))
				.thenReturn(Optional.of(token.getRefreshToken()));

			when(tokenService.parseToken(anyString()))
				.thenReturn(tokenClaim);

			when(tokenService.generateToken(any(TokenClaim.class)))
				.thenReturn(token);

			Token refreshedToken = authService.refreshToken(token.getRefreshToken());

			assertThat(refreshedToken.getAccessToken()).isNotNull();
			assertThat(refreshedToken.getRefreshToken()).isNotNull();
		}

		@Test
		@DisplayName("실패 - 토큰 만료")
		void fail_when_refresh_token_expired() {
			when(tokenQuery.getRefreshToken(anyString()))
				.thenReturn(Optional.empty());
			
			assertThatThrownBy(() -> authService.refreshToken(token.getRefreshToken()))
				.isInstanceOf(JavaKataException.class)
				.hasMessageContaining(ErrorType.AUTHENTICATION_ERROR.getMessage());
		}
	}
}
