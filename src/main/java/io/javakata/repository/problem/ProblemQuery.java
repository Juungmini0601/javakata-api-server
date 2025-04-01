package io.javakata.repository.problem;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
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

	@Transactional(readOnly = true)
	public List<Level> getDistinctLevels() {
		return problemRepository.getDistinctLevels();
	}

	@Transactional(readOnly = true)
	public Page<ProblemWithCategory> getProblems(ProblemListSearchParam param) {
		return problemRepository.getProblems(param);
	}
}
