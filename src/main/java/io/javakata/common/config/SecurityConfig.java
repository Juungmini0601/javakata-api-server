package io.javakata.common.config;

import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.javakata.common.error.ErrorType;
import io.javakata.common.filter.JwtAuthenticationFilter;
import io.javakata.common.response.ApiResponse;
import io.javakata.service.auth.TokenService;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	private static final String[] AUTH_ALLOWLIST = {
		"/swagger-ui/**", "/images/**",
	};

	private final TokenService tokenService;
	private final ObjectMapper objectMapper;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.csrf(AbstractHttpConfigurer::disable);
		http.cors(Customizer.withDefaults());

		http.sessionManagement(sessionManagement ->
			sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		);
		http.formLogin(AbstractHttpConfigurer::disable);

		http.addFilterBefore(new JwtAuthenticationFilter(tokenService), UsernamePasswordAuthenticationFilter.class);

		http.authorizeHttpRequests(authorize -> authorize
			.requestMatchers(AUTH_ALLOWLIST).permitAll()
			.requestMatchers(HttpMethod.POST, "/api/*/users").permitAll()
			.requestMatchers(HttpMethod.POST, "/api/*/auth/token").permitAll()
			.requestMatchers(HttpMethod.POST, "/api/*/auth/token/refresh").permitAll()
			.requestMatchers(HttpMethod.GET, "/api/*/problems/categories").permitAll()
			.requestMatchers("/api/*/admin/**").hasRole("ADMIN")
			.anyRequest().authenticated()
		);

		http.exceptionHandling((handling) -> handling.authenticationEntryPoint(authenticationEntryPoint())
			.accessDeniedHandler(accessDeniedHandler()));

		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public AuthenticationEntryPoint authenticationEntryPoint() {
		return (request, response, authException) -> {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setCharacterEncoding(StandardCharsets.UTF_8.name());
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			ApiResponse<?> error = ApiResponse.error(ErrorType.AUTHENTICATION_ERROR);

			response.getWriter().write(objectMapper.writeValueAsString(error));
		};
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler() {
		return (request, response, accessDeniedException) -> {
			response.setStatus(HttpStatus.FORBIDDEN.value());
			response.setCharacterEncoding(StandardCharsets.UTF_8.name());
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			ApiResponse<?> error = ApiResponse.error(ErrorType.AUTHORIZATION_ERROR);

			response.getWriter().write(objectMapper.writeValueAsString(error));
		};
	}
}