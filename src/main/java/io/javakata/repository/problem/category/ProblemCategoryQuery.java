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
public class ProblemCategoryQuery {
	private final ProblemCategoryRepository problemCategoryRepository;

	@Transactional(readOnly = true)
	public boolean existsByName(final String name) {
		return problemCategoryRepository.existsByName(name);
	}
}
