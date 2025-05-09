package io.javakata.repository.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 23.
 */
@Getter
@AllArgsConstructor
public class Token {
	private String accessToken;
	private String refreshToken;
}
