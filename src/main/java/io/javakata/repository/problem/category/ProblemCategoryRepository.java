package io.javakata.repository.problem.category;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 25.
 */
public interface ProblemCategoryRepository extends JpaRepository<ProblemCategory, Long> {
	boolean existsByName(String name);
}
