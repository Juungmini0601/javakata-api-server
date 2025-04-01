package io.javakata.service.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.javakata.common.error.ErrorType;
import io.javakata.common.error.JavaKataException;
import io.javakata.controller.user.request.CreateUserRequest;
import io.javakata.repository.user.User;
import io.javakata.repository.user.UserCommand;
import io.javakata.repository.user.UserQuery;
import lombok.RequiredArgsConstructor;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 23.
 *
 * 1. 최초는 단일 서비스로 가는데 기능 많아지면 Query, Command 분리할거임
 */
@Service
@RequiredArgsConstructor
public class UserService {

	private final PasswordEncoder passwordEncoder;
	private final UserCommand userCommand;
	private final UserQuery userQuery;

	@Transactional
	public User register(CreateUserRequest request) {
		final String email = request.email();
		if (userQuery.existsByEmail(email)) {
			throw new JavaKataException(ErrorType.CONFLICT_ERROR, "duplicated email:" + email);
		}

		final String encryptedPassword = passwordEncoder.encode(request.password());
		User user = User.withRegisterInfo(email, encryptedPassword, request.nickname());

		return userCommand.save(user);
	}

	@Transactional(readOnly = true)
	public User fetchUserByEmail(final String email) {
		return userQuery.findByEmail(email)
			.orElseThrow(() -> new JavaKataException(ErrorType.AUTHENTICATION_ERROR, "not found user"));
	}

	@Transactional
	public void deleteUser(final String email) {
		userCommand.delete(email);
	}
}
