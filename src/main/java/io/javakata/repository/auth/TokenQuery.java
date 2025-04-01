package io.javakata.repository.auth;

import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

// TODO Redis 테스트는 고민 좀 해보겠음
@Repository
@RequiredArgsConstructor
public class TokenQuery {
	private final RedisTemplate<String, String> redisTemplate;

	public Optional<String> getRefreshToken(String accessToken) {
		return Optional.ofNullable(redisTemplate.opsForValue().get(accessToken));
	}
}