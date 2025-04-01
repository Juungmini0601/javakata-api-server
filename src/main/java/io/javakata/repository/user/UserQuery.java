package io.javakata.repository.user;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 23.
 */
@Repository
@RequiredArgsConstructor
public class UserQuery {

	private final UserRepository userRepository;

	public boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}
}
