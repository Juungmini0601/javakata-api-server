package io.javakata.repository.problem.category;

import io.javakata.repository.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 25.
 */
@Builder
@ToString
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "problem_categories")
public class ProblemCategory extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, name = "category_name", unique = true)
	private String name;

	public static ProblemCategory ofName(String name) {
		return ProblemCategory.builder()
			.name(name)
			.build();
	}
}
