package io.javakata.repository.user;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 23.
 */
public interface UserRepository extends JpaRepository<User, Long> {
	boolean existsByEmail(String email);
}
