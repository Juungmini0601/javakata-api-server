package io.javakata.controller.auth;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.javakata.common.response.ApiResponse;
import io.javakata.controller.auth.request.SigninRequest;
import io.javakata.controller.auth.request.TokenRefreshRequest;
import io.javakata.controller.auth.response.SinginResponse;
import io.javakata.controller.auth.response.TokenRefreshResponse;
import io.javakata.repository.auth.Token;
import io.javakata.service.auth.AuthService;
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
