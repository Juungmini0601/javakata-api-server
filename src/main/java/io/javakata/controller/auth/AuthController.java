package io.javakata.controller.auth;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.javakata.common.response.ApiResponse;
import io.javakata.controller.auth.request.SigninRequest;
import io.javakata.controller.auth.request.TokenRefreshRequest;
import io.javakata.controller.auth.response.GetAuthResponse;
import io.javakata.controller.auth.response.SinginResponse;
import io.javakata.controller.auth.response.TokenRefreshResponse;
import io.javakata.controller.user.UserMapper;
import io.javakata.repository.auth.Token;
import io.javakata.repository.user.User;
import io.javakata.service.auth.AuthService;
import io.javakata.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 23.
 */
@RestController
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;
	private final UserService userService;

	@GetMapping("/api/v1/auth")
	public ApiResponse<GetAuthResponse> getAuth(Authentication authentication) {
		final String email = authentication.getName();
		User user = userService.fetchUserByEmail(email);

		return ApiResponse.success(UserMapper.INSTANCE.toGetAuthResponse(user));
	}

	@PostMapping("/api/v1/auth/token")
	public ApiResponse<SinginResponse> signin(@Valid @RequestBody SigninRequest request) {
		Token token = authService.generateToken(request);
		return ApiResponse.success(new SinginResponse(token.getAccessToken(), token.getRefreshToken()));
	}

	@PostMapping("/api/v1/auth/token/refresh")
	public ApiResponse<TokenRefreshResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
		Token token = authService.refreshToken(request.accessToken());
		return ApiResponse.success(new TokenRefreshResponse(token.getAccessToken(), token.getRefreshToken()));
	}
}
