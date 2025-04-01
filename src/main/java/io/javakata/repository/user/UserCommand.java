package io.javakata.repository.user;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 23.
 */
@Repository
@RequiredArgsConstructor
public class UserCommand {

	private final UserRepository userRepository;

	@Transactional
	public User save(User user) {
		return userRepository.save(user);
	}
}
