package io.javakata.repository.problem;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 27.
 */
@Repository
@RequiredArgsConstructor
public class ProblemCommand {

	private final ProblemRepository problemRepository;

	public Problem save(Problem problem) {
		return problemRepository.save(problem);
	}

	public void deleteById(Long id) {
		problemRepository.deleteById(id);
	}
}
