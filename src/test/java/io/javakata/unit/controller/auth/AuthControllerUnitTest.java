package io.javakata.unit.controller.auth;

import static io.javakata.fixtrue.FixtureUtil.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.javakata.common.config.SecurityConfig;
import io.javakata.common.error.ErrorType;
import io.javakata.common.error.JavaKataException;
import io.javakata.controller.auth.AuthController;
import io.javakata.controller.auth.request.SigninRequest;
import io.javakata.controller.auth.request.TokenRefreshRequest;
import io.javakata.repository.auth.Token;
import io.javakata.repository.user.Role;
import io.javakata.repository.user.User;
import io.javakata.service.auth.AuthService;
import io.javakata.service.auth.TokenService;
import io.javakata.service.user.UserService;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 23.
 */
@Import({SecurityConfig.class})
@WebMvcTest(AuthController.class)
public class AuthControllerUnitTest {

	final String RESULT_SUCCESS = "SUCCESS";
	final String RESULT_ERROR = "ERROR";

	@MockitoBean
	private AuthService authService;

	@MockitoBean
	private TokenService tokenService;

	@MockitoBean
	private UserService userService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Nested
	@DisplayName("로그인 단위 테스트")
	class SignInTest {
		final String baseEndPoint = "/api/v1/auth/token";
		Token token;
		SigninRequest request;

		@BeforeEach
		void setup() {
			request = defaultSigninRequest();
			token = defaultToken();
		}

		@Test
		@DisplayName("성공")
		void success() throws Exception {
			given(authService.generateToken(any(SigninRequest.class)))
				.willReturn(token);

			mockMvc.perform(post(baseEndPoint)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.result").value(RESULT_SUCCESS))
				.andExpect(jsonPath("$.data.accessToken").exists())
				.andExpect(jsonPath("$.data.refreshToken").exists());
		}

		@Test
		@DisplayName("실패 - 인증 실패")
		void fail_when_authentication_fail() throws Exception {
			given(authService.generateToken(any(SigninRequest.class)))
				.willThrow(new JavaKataException(ErrorType.AUTHENTICATION_ERROR));

			mockMvc.perform(post(baseEndPoint)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andDo(print())
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.result").value(RESULT_ERROR))
				.andExpect(jsonPath("$.error.code").value(ErrorType.AUTHENTICATION_ERROR.getCode().toString()));
		}
	}

	@Nested
	@DisplayName("토큰 재발급 단위 테스트")
	class RefreshTokenTest {
		final String baseEndPoint = "/api/v1/auth/token/refresh";
		Token token;
		TokenRefreshRequest request;

		@BeforeEach
		void setup() {
			request = defaultTokenRefreshRequest();
			token = defaultToken();
		}

		@Test
		@DisplayName("성공")
		void success() throws Exception {
			given(authService.refreshToken(anyString()))
				.willReturn(token);

			mockMvc.perform(post(baseEndPoint)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.result").value(RESULT_SUCCESS))
				.andExpect(jsonPath("$.data.accessToken").exists())
				.andExpect(jsonPath("$.data.refreshToken").exists());
		}

		@Test
		@DisplayName("실패 - 인증 실패")
		void fail_when_authentication_fail() throws Exception {
			given(authService.refreshToken(anyString()))
				.willThrow(new JavaKataException(ErrorType.AUTHENTICATION_ERROR));

			mockMvc.perform(post(baseEndPoint)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andDo(print())
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.result").value(RESULT_ERROR))
				.andExpect(jsonPath("$.error.code").value(ErrorType.AUTHENTICATION_ERROR.getCode().toString()));
		}
	}

	@Nested
	@DisplayName("인증 정보 조회 단위 테스트")
	class GetAuthTest {
		final String baseEndPoint = "/api/v1/auth";

		User user;

		@Test
		@DisplayName("성공")
		@WithMockUser(username = "testuser@email.com")
		void success() throws Exception {
			user = userFromEmailAndRole("testuser@email.com", Role.ROLE_USER);

			when(userService.fetchUserByEmail(anyString()))
				.thenReturn(user);

			mockMvc.perform(get(baseEndPoint))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.result").value(RESULT_SUCCESS))
				.andExpect(jsonPath("$.data.id").isNumber())
				.andExpect(jsonPath("$.data.email").value(user.getEmail()))
				.andExpect(jsonPath("$.data.nickname").value(user.getNickname()))
				.andExpect(jsonPath("$.data.role").value(Role.ROLE_USER.toString()))
				.andExpect(jsonPath("$.data.password").doesNotExist());
		}

		@Test
		@DisplayName("실패 - 찾을 수 없는 회원 정보")
		@WithMockUser(username = "testuser@email.com")
		void fail_user_not_found() throws Exception {
			given(userService.fetchUserByEmail(anyString()))
				.willThrow(new JavaKataException(ErrorType.AUTHENTICATION_ERROR));

			mockMvc.perform(get(baseEndPoint))
				.andDo(print())
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.result").value(RESULT_ERROR))
				.andExpect(jsonPath("$.error.code").value(ErrorType.AUTHENTICATION_ERROR.getCode().toString()));
		}
	}
}
