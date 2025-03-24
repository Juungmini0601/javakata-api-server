package io.javakata.unit.common.filter;

import static io.javakata.fixtrue.FixtureUtil.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import io.javakata.common.filter.JwtAuthenticationFilter;
import io.javakata.repository.auth.Token;
import io.javakata.repository.auth.TokenClaim;
import io.javakata.repository.user.User;
import io.javakata.service.auth.TokenService;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 24.
 */
@ExtendWith(MockitoExtension.class)
public class JwtAuthenticationFilterUnitTest {
	@Mock
	private TokenService tokenService;

	@InjectMocks
	private JwtAuthenticationFilter filter;

	User user;
	Token token;

	@BeforeEach
	void setUp() {
		SecurityContextHolder.clearContext();
		token = defaultToken();
		user = defaultUserWithOutId();
	}

	@Test
	@DisplayName("헤더가 토큰을 가지면 Authentication을 세팅한다.")
	void success() throws Exception {
		TokenClaim tokenClaim = new TokenClaim(user.getEmail(), List.of(user.getRole().toString()));
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.addHeader("Authorization", "Bearer " + token.getAccessToken());
		MockHttpServletResponse response = new MockHttpServletResponse();
		MockFilterChain chain = new MockFilterChain();

		when(tokenService.parseToken(anyString())).thenReturn(tokenClaim);

		filter.doFilter(request, response, chain);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		assertThat(authentication.getName()).isEqualTo(user.getEmail());
		assertThat(authentication.getAuthorities()).hasSize(1);
		assertThat(authentication.getAuthorities().iterator().next().getAuthority())
			.isEqualTo(user.getRole().toString());
	}
}
