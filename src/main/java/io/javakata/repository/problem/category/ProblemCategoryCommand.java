package io.javakata.repository.problem.category;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 25.
 */
@Repository
@RequiredArgsConstructor
public class ProblemCategoryCommand {
	private final ProblemCategoryRepository problemCategoryRepository;

	@Transactional
	public ProblemCategory save(ProblemCategory problemCategory) {
		return problemCategoryRepository.save(problemCategory);
	}

	@Transactional
	public void deleteById(Long id) {
		problemCategoryRepository.deleteById(id);
	}
}
