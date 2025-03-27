package io.javakata.repository.problem;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 27.
 */
@Repository
@RequiredArgsConstructor
public class ProblemQuery {

	private final ProblemRepository problemRepository;

	@Transactional(readOnly = true)
	public Optional<Problem> findById(Long id) {
		return problemRepository.findById(id);
	}
}
