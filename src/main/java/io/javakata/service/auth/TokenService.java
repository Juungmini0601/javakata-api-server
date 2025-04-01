package io.javakata.service.auth;

import io.javakata.repository.auth.Token;
import io.javakata.repository.auth.TokenClaim;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 23.
 */
public interface TokenService {
	String generateAccessToken(TokenClaim tokenClaim);

	String generateRefreshToken(TokenClaim tokenClaim);

	Token generateToken(TokenClaim tokenClaim);

	TokenClaim parseToken(String token);
}
