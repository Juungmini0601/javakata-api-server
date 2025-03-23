package io.javakata.unit.service.auth;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import io.javakata.common.config.JwtConfig;
import io.javakata.repository.auth.Token;
import io.javakata.repository.auth.TokenClaim;
import io.javakata.service.auth.JwtTokenService;
import io.javakata.service.auth.TokenService;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 23.
 */
public class JwtTokenServiceUnitTest {

	private static final String ACCESS_TOKEN_SECRET = "jsdhfgsddfaskdfgkasdghfjkadkjfagdjadfgasjdfgaskdjfgkasjdasdfhjasdfg";
	private static final Long ACCESS_TOKEN_EXPIRE_TIME = 3600000L;
	private static final String REFRESH_TOKEN_SECRET = "jsdhfgsddfaskdfgkasdghfjkadkjfagdjadfgasjdfgaskdjfgkasjdasdfhjasdfg";
	private static final Long REFRESH_TOKEN_EXPIRE_TIME = 604800000L;

	private JwtConfig jwtConfig = new JwtConfig(
		new JwtConfig.AccessToken(ACCESS_TOKEN_SECRET, ACCESS_TOKEN_EXPIRE_TIME),
		new JwtConfig.RefreshToken(REFRESH_TOKEN_SECRET, REFRESH_TOKEN_EXPIRE_TIME)
	);

	private TokenService tokenService = new JwtTokenService(jwtConfig);

	@Test
	void generateAccessTokenTest() {
		TokenClaim tokenClaim = new TokenClaim("test", List.of("ROLE_USER"));

		String accessToken = tokenService.generateAccessToken(tokenClaim);

		assertThat(accessToken).isNotNull();
	}

	@Test
	void generateRefreshTokenTest() {
		TokenClaim tokenClaim = new TokenClaim("test", List.of("ROLE_USER"));

		String refreshToken = tokenService.generateRefreshToken(tokenClaim);

		assertThat(refreshToken).isNotNull();
	}

	@Test
	void parseTokenTest() {
		TokenClaim tokenClaim = new TokenClaim("test", List.of("ROLE_USER"));
		Token token = tokenService.generateToken(tokenClaim);

		TokenClaim parsedTokenClaim = tokenService.parseToken(token.getAccessToken());

		assertThat(parsedTokenClaim.getRoles().get(0)).isEqualTo("ROLE_USER");
		assertThat(parsedTokenClaim.getSubject()).isEqualTo("test");
	}
}
