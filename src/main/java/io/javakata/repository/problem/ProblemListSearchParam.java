package io.javakata.repository.problem;

import java.util.List;

import org.springframework.data.domain.Pageable;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 28
 */
@Getter
@AllArgsConstructor
public class ProblemListSearchParam {
	private Pageable pageable;
	private String keyword;
	private List<String> categories;
	private List<String> levels;
}
