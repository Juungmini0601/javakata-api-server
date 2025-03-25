package io.javakata.controller.problem.category;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import io.javakata.controller.problem.category.response.CreateProblemCategoryResponse;
import io.javakata.controller.problem.category.response.GetProblemCategoryResponse;
import io.javakata.controller.problem.category.response.UpdateProblemCategoryResponse;
import io.javakata.repository.problem.category.ProblemCategory;

/**
 * @author    : kimjungmin
 * Created on : 2025. 3. 25.
 */
// TODO 패키지 위치가 상당히 애매해질 것 같은 느낌.
@Mapper
public interface ProblemCategoryMapper {
	ProblemCategoryMapper INSTANCE = Mappers.getMapper(ProblemCategoryMapper.class);

	@Mapping(source = "name", target = "categoryName")
	CreateProblemCategoryResponse toCreateProblemCategoryResponse(ProblemCategory category);

	@Mapping(source = "name", target = "categoryName")
	UpdateProblemCategoryResponse toUpdateProblemCategoryResponse(ProblemCategory category);

	@Mapping(source = "name", target = "categoryName")
	GetProblemCategoryResponse toGetProblemCategoryResponse(ProblemCategory category);
}
