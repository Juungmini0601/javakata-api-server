package io.javakata.repository.problem.category;

import java.util.List;
import java.util.Optional;

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

	@Transactional(readOnly = true)
	public Optional<ProblemCategory> findById(final Long id) {
		return problemCategoryRepository.findById(id);
	}

	@Transactional
	public List<ProblemCategory> findAll() {
		return problemCategoryRepository.findAll();
	}
}
