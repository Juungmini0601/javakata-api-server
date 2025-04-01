package io.javakata.unit.controller.user;

import static io.javakata.fixtrue.FixtureUtil.*;
import static org.mockito.ArgumentMatchers.*;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.javakata.common.config.SecurityConfig;
import io.javakata.common.error.ErrorType;
import io.javakata.common.error.JavaKataException;
import io.javakata.controller.user.UserController;
import io.javakata.controller.user.request.CreateUserRequest;
import io.javakata.repository.user.Role;
import io.javakata.repository.user.User;
import io.javakata.service.auth.TokenService;
import io.javakata.service.user.UserService;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 23.
 */
@Import({SecurityConfig.class})
@WebMvcTest({UserController.class})
public class UserControllerUnitTest {

	final String RESULT_SUCCESS = "SUCCESS";
	final String RESULT_ERROR = "ERROR";

	// WebMvcTest는 의존성이 끊겨서 이렇게 사용해야함.
	@MockitoBean
	private UserService userService;

	@MockitoBean
	private TokenService tokenService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Nested
	@DisplayName("회원 생성 단위 테스트")
	class CreateUserTest {

		final String baseEndPoint = "/api/v1/users";
		CreateUserRequest request;

		@BeforeEach
		void setUp() {
			request = defaultCreateUserRequest();
		}

		@Test
		@DisplayName("성공")
		void success() throws Exception {
			User user = userFromCreateUserRequest(request);
			given(userService.register(any(CreateUserRequest.class))).willReturn(user);

			mockMvc.perform(post(baseEndPoint)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.result").value(RESULT_SUCCESS))
				.andExpect(jsonPath("$.data.id").isNumber())
				.andExpect(jsonPath("$.data.nickname").value(request.nickname()))
				.andExpect(jsonPath("$.data.email").value(request.email()))
				.andExpect(jsonPath("$.data.role").value(Role.ROLE_USER.toString()))
				.andExpect(jsonPath("$.data.password").doesNotExist());
		}

		@Test
		@DisplayName("실패 - 중복된 이메일")
		void fail_when_email_is_duplicated() throws Exception {
			given(userService.register(any(CreateUserRequest.class)))
				.willThrow(new JavaKataException(ErrorType.CONFLICT_ERROR, "duplicated email:" + request.email()));

			mockMvc.perform(post(baseEndPoint)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andDo(print())
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.result").value(RESULT_ERROR))
				.andExpect(jsonPath("$.error.code").value(ErrorType.CONFLICT_ERROR.getCode().toString()));
		}
	}

	@Nested
	@DisplayName("회원 탈퇴 단위 테스트")
	class DeleteUserTest {
		final String baseEndPoint = "/api/v1/users";

		@Test
		@DisplayName("성공")
		@WithMockUser(username = "testuser@email.com")
		void success() throws Exception {
			String email = SecurityContextHolder.getContext().getAuthentication().getName();
			doNothing().when(userService).deleteUser(email);

			mockMvc.perform(delete(baseEndPoint))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.result").value(RESULT_SUCCESS));
		}

		@Test
		@DisplayName("실패 - 인증되지 않은 유저")
		void fail_when_authentication_empty() throws Exception {

			mockMvc.perform(delete(baseEndPoint))
				.andDo(print())
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.result").value(RESULT_ERROR));
		}
	}
}
