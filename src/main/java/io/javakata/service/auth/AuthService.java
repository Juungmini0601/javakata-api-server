package io.javakata.service.auth;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import io.javakata.common.error.ErrorType;
import io.javakata.common.error.JavaKataException;
import io.javakata.controller.auth.request.SigninRequest;
import io.javakata.repository.auth.Token;
import io.javakata.repository.auth.TokenClaim;
import io.javakata.repository.auth.TokenCommand;
import io.javakata.repository.auth.TokenQuery;
import io.javakata.repository.user.User;
import io.javakata.repository.user.UserQuery;
import lombok.RequiredArgsConstructor;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 23.
 */
@Service
@RequiredArgsConstructor
public class AuthService {
	private final UserQuery userQuery;
	private final TokenQuery tokenQuery;
	private final TokenService tokenService;
	private final TokenCommand tokenCommand;
	private final PasswordEncoder passwordEncoder;

	public Token generateToken(SigninRequest request) {
		User user = userQuery.findByEmail(request.email())
			.orElseThrow(() -> new JavaKataException(ErrorType.AUTHENTICATION_ERROR, "Invalid Authentication Info"));

		if (!passwordEncoder.matches(request.password(), user.getPassword())) {
			throw new JavaKataException(ErrorType.AUTHENTICATION_ERROR, "Invalid Authentication Info");
		}

		TokenClaim tokenClaim = new TokenClaim(user.getEmail(), List.of(user.getRole().toString()));
		Token token = tokenService.generateToken(tokenClaim);

		tokenCommand.appendToken(token);

		return token;
	}

	public Token refreshToken(String accessToken) {
		String refreshToken = tokenQuery.getRefreshToken(accessToken)
			.orElseThrow(() -> new JavaKataException(ErrorType.AUTHENTICATION_ERROR, "Invalid Refresh Token"));

		TokenClaim tokenClaim = tokenService.parseToken(refreshToken);
		Token token = tokenService.generateToken(tokenClaim);

		tokenCommand.appendToken(token);

		return token;
	}
}
