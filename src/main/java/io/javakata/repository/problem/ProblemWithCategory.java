package io.javakata.repository.problem;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 5.
 */
@Getter
@AllArgsConstructor
public class ProblemWithCategory {
	private Long id;
	private String title;
	private String level;
	private String categoryName;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
