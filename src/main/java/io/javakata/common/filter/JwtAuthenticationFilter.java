package io.javakata.common.filter;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.javakata.repository.auth.TokenClaim;
import io.javakata.service.auth.TokenService;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 24.
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final TokenService tokenService;
	private static final String AUTHENTICATION_HEADER = "Authorization";
	private static final String JWT_TOKEN_PREFIX = "Bearer ";

	public JwtAuthenticationFilter(TokenService tokenService) {
		this.tokenService = tokenService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		String authorizationHeader = request.getHeader(AUTHENTICATION_HEADER);
		if (StringUtils.isNotBlank(authorizationHeader) && authorizationHeader.startsWith(JWT_TOKEN_PREFIX)) {
			String accessToken = authorizationHeader.substring(7);
			TokenClaim tokenClaim = tokenService.parseToken(accessToken);
			List<SimpleGrantedAuthority> authorities = tokenClaim.getRoles()
				.stream()
				.map(SimpleGrantedAuthority::new)
				.toList();

			Authentication authentication = new TestingAuthenticationToken(tokenClaim.getSubject(), "password",
				authorities);
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		filterChain.doFilter(request, response);
	}
}
