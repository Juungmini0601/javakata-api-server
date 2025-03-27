package io.javakata.repository.problem;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 27.
 */
public interface ProblemRepository extends JpaRepository<Problem, Long>, ProblemSearchRepository {
}
