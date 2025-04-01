package io.javakata.repository.user;

import java.util.Optional;

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

	public Optional<User> findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
}
