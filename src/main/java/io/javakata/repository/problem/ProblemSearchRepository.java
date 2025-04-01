package io.javakata.repository.problem;

import java.util.List;

import org.springframework.data.domain.Page;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 18.
 */
public interface ProblemSearchRepository {
	Page<ProblemWithCategory> getProblems(ProblemListSearchParam param);

	List<Level> getDistinctLevels();
}
