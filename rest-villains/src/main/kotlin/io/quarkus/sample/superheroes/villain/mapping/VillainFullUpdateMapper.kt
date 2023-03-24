package io.quarkus.sample.superheroes.villain.mapping

import io.quarkus.sample.superheroes.villain.Villain
import org.mapstruct.Mapper
import org.mapstruct.Mapping
import org.mapstruct.MappingTarget

// Mapper to map all fields on an input onto a target.
@Mapper(componentModel = "cdi")
interface VillainFullUpdateMapper {
  // Maps all fields except id from input onto target.
  @Mapping(target = "id", ignore = true)
  fun mapFullUpdate(input: Villain, @MappingTarget target: Villain)
}
