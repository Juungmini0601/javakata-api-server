package io.javakata.repository.auth;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import io.javakata.common.config.JwtConfig;
import lombok.RequiredArgsConstructor;

// TODO Redis 테스트는 고민 좀 해보겠음
@Repository
@RequiredArgsConstructor
public class TokenCommand {
	private final JwtConfig jwtConfig;
	private final RedisTemplate<String, String> redisTemplate;

	public void appendToken(Token token) {
		redisTemplate.opsForValue()
			.set(
				token.getAccessToken(),
				token.getRefreshToken(),
				jwtConfig.getRefreshToken().expire(),
				TimeUnit.MICROSECONDS
			);
	}
}