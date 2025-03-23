package io.javakata.service.auth;

import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

import io.javakata.common.config.JwtConfig;
import io.javakata.repository.auth.Token;
import io.javakata.repository.auth.TokenClaim;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 23.
 */
@Service
@RequiredArgsConstructor
public class JwtTokenService implements TokenService {

	private final JwtConfig jwtConfig;

	@Override
	public String generateAccessToken(TokenClaim tokenClaim) {
		long now = System.currentTimeMillis();
		Date expireDate = new Date(now + jwtConfig.getAccessToken().expire());
		SecretKey secretKey = Keys.hmacShaKeyFor(jwtConfig.getAccessToken().secret().getBytes());

		return Jwts.builder()
			.subject(tokenClaim.getSubject())
			.claim("roles", tokenClaim.getRoles())
			.issuedAt(new Date(now))
			.expiration(expireDate)
			.signWith(secretKey)
			.compact();
	}

	@Override
	public String generateRefreshToken(TokenClaim tokenClaim) {
		long now = System.currentTimeMillis();
		Date expireDate = new Date(now + jwtConfig.getRefreshToken().expire());
		SecretKey secretKey = Keys.hmacShaKeyFor(jwtConfig.getRefreshToken().secret().getBytes());

		return Jwts.builder()
			.subject(tokenClaim.getSubject())
			.claim("roles", tokenClaim.getRoles())
			.issuedAt(new Date(now))
			.expiration(expireDate)
			.signWith(secretKey)
			.compact();
	}

	@Override
	public Token generateToken(TokenClaim tokenClaim) {
		String accessToken = generateAccessToken(tokenClaim);
		String refreshToken = generateRefreshToken(tokenClaim);
		return new Token(accessToken, refreshToken);
	}

	@Override
	public TokenClaim parseToken(String token) {
		SecretKey secretKey = Keys.hmacShaKeyFor(jwtConfig.getRefreshToken().secret().getBytes());
		Jws<Claims> claimsJws = Jwts.parser().verifyWith(secretKey).build()
			.parseSignedClaims(token);

		String email = claimsJws.getPayload().getSubject();
		List<?> roles = claimsJws.getPayload().get("roles", List.class);

		return new TokenClaim(email, roles.stream().map(Object::toString).toList());
	}
}
