package io.javakata.entity.submission;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 17.
 */
public enum Status {
	PENDING,
	RUNNING,
	SUCCESS,
	FAILED,
	TIME_EXCEED,
	MEMORY_EXCEED,
	COMPILE_ERROR,
	RUNTIME_ERROR,
	INTERNAL_ERROR
}
