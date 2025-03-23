package io.javakata.fixtrue;

import net.jqwik.api.Arbitraries;

import io.javakata.controller.auth.request.SigninRequest;
import io.javakata.controller.auth.request.TokenRefreshRequest;
import io.javakata.controller.user.request.CreateUserRequest;
import io.javakata.repository.auth.Token;
import io.javakata.repository.auth.TokenClaim;
import io.javakata.repository.user.OAuthProvider;
import io.javakata.repository.user.Role;
import io.javakata.repository.user.User;

import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.ConstructorPropertiesArbitraryIntrospector;
import com.navercorp.fixturemonkey.jakarta.validation.plugin.JakartaValidationPlugin;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 23.
 */
public class FixtureUtil {
	private static final FixtureMonkey beanValidationFixtureMonkey = FixtureMonkey.builder()
		.objectIntrospector(ConstructorPropertiesArbitraryIntrospector.INSTANCE)
		.plugin(new JakartaValidationPlugin())
		.defaultNotNull(true)
		.build();

	public static User defaultUser() {
		return beanValidationFixtureMonkey.giveMeOne(User.class);
	}

	public static User defaultUserWithOutId() {
		return beanValidationFixtureMonkey.giveMeBuilder(User.class)
			.set("id", null)
			.set("email", Arbitraries.strings().withCharRange('a', 'z').ofMinLength(10).ofMaxLength(200))
			.set("password", Arbitraries.strings().withCharRange('a', 'z').ofMinLength(10).ofMaxLength(200))
			.set("nickname", Arbitraries.strings().withCharRange('a', 'z').ofMinLength(10).ofMaxLength(200))
			.set("role", Role.ROLE_USER)
			.set("oAuthProvider", OAuthProvider.LOCAL)
			.sample();
	}

	public static User userFromCreateUserRequest(CreateUserRequest request) {
		return beanValidationFixtureMonkey.giveMeBuilder(User.class)
			.set("id", Arbitraries.longs().between(1L, 10L))
			.set("email", request.email())
			.set("password", request.password())
			.set("nickname", request.nickname())
			.set("role", Role.ROLE_USER)
			.set("oAuthProvider", OAuthProvider.LOCAL)
			.sample();
	}

	public static User userFromSigninRequest(SigninRequest request) {
		return beanValidationFixtureMonkey.giveMeBuilder(User.class)
			.set("id", Arbitraries.longs().between(1L, 10L))
			.set("email", request.email())
			.set("password", request.password())
			.set("role", Role.ROLE_USER)
			.set("oAuthProvider", OAuthProvider.LOCAL)
			.sample();
	}

	public static TokenClaim defaultTokenClaim() {
		return beanValidationFixtureMonkey.giveMeOne(TokenClaim.class);
	}

	public static Token defaultToken() {
		return beanValidationFixtureMonkey.giveMeOne(Token.class);
	}

	public static CreateUserRequest defaultCreateUserRequest() {
		return beanValidationFixtureMonkey.giveMeOne(CreateUserRequest.class);
	}

	public static SigninRequest defaultSigninRequest() {
		return beanValidationFixtureMonkey.giveMeOne(SigninRequest.class);
	}

	public static TokenRefreshRequest defaultTokenRefreshRequest() {
		return beanValidationFixtureMonkey.giveMeOne(TokenRefreshRequest.class);
	}
}
