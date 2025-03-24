package io.javakata.controller.user;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import io.javakata.controller.user.response.CreateUserResponse;
import io.javakata.repository.user.User;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 23.
 */
@Mapper
public interface UserMapper {
	UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

	CreateUserResponse toCreateUserResponse(User user);
}
