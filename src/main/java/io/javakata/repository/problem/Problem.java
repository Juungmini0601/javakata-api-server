package io.javakata.repository.problem;

import io.javakata.repository.BaseEntity;
import io.javakata.repository.problem.category.ProblemCategory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 17.
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "problems")
public class Problem extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String title;

	@Enumerated(EnumType.STRING)
	private Level level;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String description;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String input;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String constraints; // 입력 제한조건

	@Column(nullable = false, columnDefinition = "TEXT")
	private String expectedOutput; // 문제의 기대 출력값 설명

	@Column(nullable = false, columnDefinition = "TEXT")
	private String baseCode;

	@ManyToOne
	@JoinColumn(name = "category_id", nullable = false)
	private ProblemCategory category;
}
