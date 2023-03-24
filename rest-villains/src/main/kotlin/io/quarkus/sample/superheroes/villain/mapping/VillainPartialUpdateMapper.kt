package io.quarkus.sample.superheroes.villain.mapping

import io.quarkus.sample.superheroes.villain.Villain
import org.mapstruct.Mapper
import org.mapstruct.MappingTarget
import org.mapstruct.NullValuePropertyMappingStrategy

// Mapper to map non-null fields on an input onto a target.
@Mapper(componentModel = "cdi", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
interface VillainPartialUpdateMapper {
  fun mapPartialUpdate(input: Villain, @MappingTarget target: Villain)
}
