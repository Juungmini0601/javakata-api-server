package io.javakata.unit.controller.problem.category;

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
import io.javakata.controller.problem.category.AdminProblemCategoryController;
import io.javakata.controller.problem.category.request.CreateProblemCategoryRequest;
import io.javakata.controller.problem.category.request.UpdateProblemCategoryRequest;
import io.javakata.repository.problem.category.ProblemCategory;
import io.javakata.service.auth.TokenService;
import io.javakata.service.problem.category.ProblemCategoryService;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 25.
 */
@Import({SecurityConfig.class})
@WebMvcTest(AdminProblemCategoryController.class)
public class AdminProblemCategoryControllerUnitTest {

	final String RESULT_SUCCESS = "SUCCESS";
	final String RESULT_ERROR = "ERROR";

	@MockitoBean
	private ProblemCategoryService problemCategoryService;

	@MockitoBean
	private TokenService tokenService;

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Nested
	@DisplayName("어드민 - 문제 생성 단위 테스트")
	class CreateProblemCategoryTest {

		final String baseEndPoint = "/api/v1/admin/problems/categories";

		CreateProblemCategoryRequest request;
		ProblemCategory category;

		@BeforeEach
		void setup() {
			request = defaultCreateProblemCategoryRequest();
			category = problemCategoryFromCreateRequest(request);
		}

		@Test
		@DisplayName("성공")
		@WithMockUser(username = "testuser@email.com", roles = {"ADMIN"})
		void success() throws Exception {
			when(problemCategoryService.createCategory(any(CreateProblemCategoryRequest.class)))
				.thenReturn(category);

			mockMvc.perform(post(baseEndPoint)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.result").value(RESULT_SUCCESS))
				.andExpect(jsonPath("$.data.id").exists())
				.andExpect(jsonPath("$.data.categoryName").value(request.categoryName()));
		}

		@Test
		@DisplayName("실패 - 중복된 카테고리 이름")
		@WithMockUser(username = "testuser@email.com", roles = {"ADMIN"})
		void fail_when_category_name_is_duplicated() throws Exception {
			given(problemCategoryService.createCategory(any(CreateProblemCategoryRequest.class)))
				// @formatter:off
				.willThrow(new JavaKataException(ErrorType.CONFLICT_ERROR, "duplicated category name:" + request.categoryName()));

			mockMvc.perform(post(baseEndPoint)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andDo(print())
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.result").value(RESULT_ERROR))
				.andExpect(jsonPath("$.error.code").value(ErrorType.CONFLICT_ERROR.getCode().toString()));
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

	@Nested
	@DisplayName("어드민 - 문제 수정 단위 테스트")
	class UpdateProblemCategoryTest {

		final String baseEndPoint = "/api/v1/admin/problems/categories";

		UpdateProblemCategoryRequest request;
		ProblemCategory category;

		@BeforeEach
		void setup() {
			request = defaultUpdateProblemCategoryRequest();
			category = problemCategoryFromUpdateRequest(request);
		}

		@Test
		@DisplayName("성공")
		@WithMockUser(username = "testuser@email.com", roles = {"ADMIN"})
		void success() throws Exception {
			final Long categoryId = category.getId();
			when(problemCategoryService.updateCategory(anyLong(), any(UpdateProblemCategoryRequest.class)))
				.thenReturn(category);

			mockMvc.perform(put(baseEndPoint + "/" + categoryId)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.result").value(RESULT_SUCCESS))
				.andExpect(jsonPath("$.data.id").exists())
				.andExpect(jsonPath("$.data.categoryName").value(request.categoryName()));
		}

		@Test
		@DisplayName("실패 - 중복된 카테고리 이름")
		@WithMockUser(username = "testuser@email.com", roles = {"ADMIN"})
		void fail_when_category_name_is_duplicated() throws Exception {
			final Long categoryId = category.getId();
			given(problemCategoryService.updateCategory(anyLong(), any(UpdateProblemCategoryRequest.class)))
				// @formatter:off
				.willThrow(new JavaKataException(ErrorType.CONFLICT_ERROR, "duplicated category name:" + request.categoryName()));


			mockMvc.perform(put(baseEndPoint + "/" + categoryId)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andDo(print())
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.result").value(RESULT_ERROR))
				.andExpect(jsonPath("$.error.code").value(ErrorType.CONFLICT_ERROR.getCode().toString()));
		}

		@Test
		@DisplayName("실패 - 인증 되지 않은 유저")
		void fail_when_not_authentication() throws Exception {
			final Long categoryId = category.getId();

			mockMvc.perform(put(baseEndPoint + "/" + categoryId)
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
			final Long categoryId = category.getId();

			mockMvc.perform(put(baseEndPoint + "/" + categoryId)
					.contentType(MediaType.APPLICATION_JSON)
					.content(objectMapper.writeValueAsString(request)))
				.andDo(print())
				.andExpect(status().isForbidden())
				.andExpect(jsonPath("$.result").value(RESULT_ERROR))
				.andExpect(jsonPath("$.error.code").value(ErrorType.AUTHORIZATION_ERROR.getCode().toString()));
		}
	}

	@Nested
	@DisplayName("어드민 - 문제 수정 단위 테스트")
	class DeleteProblemCategoryTest {

		final String baseEndPoint = "/api/v1/admin/problems/categories";

		ProblemCategory category;

		@BeforeEach
		void setup() {
			category = defaultProblemCategory();
		}

		@Test
		@DisplayName("성공")
		@WithMockUser(username = "testuser@email.com", roles = {"ADMIN"})
		void success() throws Exception {
			final Long categoryId = category.getId();
			doNothing().when(problemCategoryService).deleteCategory(anyLong());

			mockMvc.perform(delete(baseEndPoint + "/" + categoryId)
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.result").value(RESULT_SUCCESS));
		}

		@Test
		@DisplayName("실패 - 인증 되지 않은 유저")
		void fail_when_not_authentication() throws Exception {
			final Long categoryId = category.getId();

			mockMvc.perform(delete(baseEndPoint + "/" + categoryId)
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isUnauthorized())
				.andExpect(jsonPath("$.result").value(RESULT_ERROR))
				.andExpect(jsonPath("$.error.code").value(ErrorType.AUTHENTICATION_ERROR.getCode().toString()));
		}


		@Test
		@DisplayName("실패 - 권한 없는 유저")
		@WithMockUser(username = "testuser@email.com")
		void fail_when_not_admin_user_request() throws Exception {
			final Long categoryId = category.getId();

			mockMvc.perform(delete(baseEndPoint + "/" + categoryId)
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isForbidden())
				.andExpect(jsonPath("$.result").value(RESULT_ERROR))
				.andExpect(jsonPath("$.error.code").value(ErrorType.AUTHORIZATION_ERROR.getCode().toString()));
		}
	}
}
