package io.javakata.controller.user;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.javakata.common.response.ApiResponse;
import io.javakata.controller.user.request.CreateUserRequest;
import io.javakata.controller.user.request.CreateUserResponse;
import io.javakata.repository.user.User;
import io.javakata.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 23.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@PostMapping("/api/v1/users")
	public ApiResponse<CreateUserResponse> register(
		@RequestBody @Valid CreateUserRequest request) {
		User registeredUser = userService.register(request);

		return ApiResponse.success(UserMapper.INSTANCE.toCreateUserResponse(registeredUser));
	}
}
