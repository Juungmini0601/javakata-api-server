package io.javakata.unit.controller.problem.category;

import static io.javakata.fixtrue.FixtureUtil.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.javakata.common.config.SecurityConfig;
import io.javakata.controller.problem.category.ProblemCategoryController;
import io.javakata.repository.problem.category.ProblemCategory;
import io.javakata.service.auth.TokenService;
import io.javakata.service.problem.category.ProblemCategoryService;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 25.
 */
@Import({SecurityConfig.class})
@WebMvcTest(ProblemCategoryController.class)
public class ProblemCategoryControllerUnitTest {
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
	@DisplayName("카테고리 목록 조회 단위 테스트")
	class ListProblemCategoryTest {
		final String baseEndPoint = "/api/v1/problems/categories";
		final int defaultSize = 10;

		List<ProblemCategory> problemCategories;

		@BeforeEach
		void setup() {
			problemCategories = defaultProblemCategories(10);
		}

		@Test
		@DisplayName("성공")
		void success() throws Exception {
			when(problemCategoryService.findAll())
				.thenReturn(problemCategories);

			mockMvc.perform(get(baseEndPoint)
					.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.result").value(RESULT_SUCCESS))
				.andExpect(jsonPath("$.data", hasSize(defaultSize)))
				.andExpect(jsonPath("$.data[0].id").value(problemCategories.get(0).getId()))
				.andExpect(jsonPath("$.data[0].categoryName").value(problemCategories.get(0).getName()));
		}
	}
}
