package io.javakata.common.error;

import lombok.Getter;

@Getter
public class JavaKataException extends RuntimeException {
	private final ErrorType errorType;
	private final Object data;

	public JavaKataException(ErrorType errorType) {
		super(errorType.getMessage());
		this.errorType = errorType;
		this.data = null;
	}

	public JavaKataException(ErrorType errorType, Object data) {
		super(errorType.getMessage());
		this.errorType = errorType;
		this.data = data;
	}
}
