package io.javakata.unit.controller.problem;

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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.javakata.common.config.SecurityConfig;
import io.javakata.common.error.ErrorType;
import io.javakata.common.error.JavaKataException;
import io.javakata.controller.problem.AdminProblemController;
import io.javakata.controller.problem.request.CreateProblemRequest;
import io.javakata.repository.problem.Problem;
import io.javakata.repository.problem.category.ProblemCategory;
import io.javakata.service.auth.TokenService;
import io.javakata.service.problem.ProblemService;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 27.
 */
@Import({SecurityConfig.class})
@WebMvcTest(AdminProblemController.class)
public class AdminProblemControllerUnitTest {

	final String RESULT_SUCCESS = "SUCCESS";
	final String RESULT_ERROR = "ERROR";

	@MockitoBean
	private ProblemService problemService;

	@MockitoBean
	private TokenService tokenService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Nested
	@DisplayName("어드민 - 문제 생성 단위 테스트")
	class CreateProblemTest {
		final String baseEndPoint = "/api/v1/admin/problems";

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
		@WithMockUser(username = "testuser@email.com", roles = {"ADMIN"})
		void success() throws Exception {
			when(problemService.createProblem(any(CreateProblemRequest.class)))
				.thenReturn(problem);

			mockMvc.perform(post(baseEndPoint)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.result").value(RESULT_SUCCESS))
				.andExpect(jsonPath("$.data.title").exists())
				.andExpect(jsonPath("$.data.level").exists())
				.andExpect(jsonPath("$.data.description").exists())
				.andExpect(jsonPath("$.data.constraints").exists())
				.andExpect(jsonPath("$.data.input").exists())
				.andExpect(jsonPath("$.data.expectedOutput").exists())
				.andExpect(jsonPath("$.data.baseCode").exists())
				.andExpect(jsonPath("$.data.categoryId").exists())
				.andExpect(jsonPath("$.data.testCases").exists());
		}

		@Test
		@DisplayName("실패 - 유효하지 않은 카테고리 아이디")
		@WithMockUser(username = "testuser@email.com", roles = {"ADMIN"})
		void fail_when_invalid_category_id() throws Exception {
			given(problemService.createProblem(any(CreateProblemRequest.class)))
				// @formatter:off
				.willThrow(new JavaKataException(ErrorType.VALIDATION_ERROR));

			mockMvc.perform(post(baseEndPoint)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.result").value(RESULT_ERROR))
				.andExpect(jsonPath("$.error.code").value(ErrorType.VALIDATION_ERROR.getCode().toString()));
		}

		@Test
		@DisplayName("실패 - 인증 되지 않은 유저")
		void fail_when_not_authentication() throws Exception {
			mockMvc.perform(post(baseEndPoint)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andDo(print())
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.result").value(RESULT_ERROR))
				.andExpect(jsonPath("$.error.code").value(ErrorType.AUTHENTICATION_ERROR.getCode().toString()));
		}

		@Test
		@DisplayName("실패 - 권한 없는 유저")
		@WithMockUser(username = "testuser@email.com")
		void fail_when_not_admin_user_request() throws Exception {
			mockMvc.perform(post(baseEndPoint)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andDo(print())
				.andExpect(status().isForbidden())
				.andExpect(jsonPath("$.result").value(RESULT_ERROR))
				.andExpect(jsonPath("$.error.code").value(ErrorType.AUTHORIZATION_ERROR.getCode().toString()));
		}
	}
}
