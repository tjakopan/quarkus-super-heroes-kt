package io.quarkus.sample.superheroes.villain.service

import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional
import io.quarkus.logging.Log
import io.quarkus.sample.superheroes.villain.Villain
import io.quarkus.sample.superheroes.villain.config.VillainConfig
import io.quarkus.sample.superheroes.villain.mapping.VillainFullUpdateMapper
import io.quarkus.sample.superheroes.villain.mapping.VillainPartialUpdateMapper
import io.smallrye.mutiny.Uni
import io.smallrye.mutiny.coroutines.awaitSuspending
import utilities.service.AbstractService
import utilities.service.callService
import utilities.service.runService
import javax.enterprise.context.ApplicationScoped
import javax.validation.ConstraintViolationException
import javax.validation.Valid
import javax.validation.Validator
import javax.validation.constraints.NotNull
import kotlin.math.roundToInt

@Suppress("CdiInjectionPointsInspection")
@ApplicationScoped
class VillainService(
  private val validator: Validator,
  private val villainConfig: VillainConfig,
  private val villainPartialUpdateMapper: VillainPartialUpdateMapper,
  private val villainFullUpdateMapper: VillainFullUpdateMapper
) : AbstractService() {
  fun findAllVillains(): Uni<List<Villain>> = callService {
    Log.debug("Getting all villains")
    Villain.listAll().awaitSuspending()
  }

  fun findAllVillainsHavingName(name: String): Uni<List<Villain>> = callService {
    Log.debug("Finding all villains having name = $name")
    Villain.listAllWhereNameLike(name)
  }

  fun findVillainById(id: Long): Uni<Villain?> = callService {
    Log.debug("Finding villain by id = $id")
    Villain.findById(id).awaitSuspending()
  }

  fun findRandomVillain(): Uni<Villain?> = callService {
    Log.debug("Finding a random villain")
    Villain.findRandom()
  }

  @ReactiveTransactional
  fun persistVillain(@NotNull @Valid villain: Villain): Uni<Villain> = callService {
    Log.debug("Persisting villain: $villain")
    villain.level = (villain.level!! * villainConfig.level().multiplier()).roundToInt()
    Villain.persist(villain).awaitSuspending()
    villain
  }

  @ReactiveTransactional
  fun replaceVillain(@NotNull @Valid villain: Villain): Uni<Villain?> = callService {
    Log.debug("Replacing villain: $villain")
    val v = if (villain.id != null) Villain.findById(villain.id!!).awaitSuspending() else null
    if (v != null) villainFullUpdateMapper.mapFullUpdate(villain, v)
    v
  }

  @ReactiveTransactional
  fun partialUpdateVillain(@NotNull villain: Villain): Uni<Villain?> = callService {
    Log.debug("Partially updating villain: $villain")
    val v = if (villain.id != null) Villain.findById(villain.id!!).awaitSuspending() else null
    if (v != null) {
      villainPartialUpdateMapper.mapPartialUpdate(villain, v)
      validatePartialUpdate(villain)
    }
    v
  }

  @ReactiveTransactional
  fun replaceAllVillains(villains: List<Villain>): Uni<Void> = runService {
    Log.debug("Replacing all villains")
    deleteAllVillains().awaitSuspending()
    Villain.persist(villains).awaitSuspending()
  }

  private fun validatePartialUpdate(villain: Villain) {
    val violations = validator.validate(villain)
    if (violations != null && violations.isNotEmpty()) throw ConstraintViolationException(violations)
  }

  @ReactiveTransactional
  fun deleteAllVillains(): Uni<Void> = runService {
    Log.debug("Deleting all villains")
    val villains = Villain.listAll().awaitSuspending()
    villains.forEach { v -> deleteVillain(v.id!!).awaitSuspending() }
  }

  @ReactiveTransactional
  fun deleteVillain(id: Long): Uni<Void> = runService {
    Log.debug("Deleting villain by id = $id")
    Villain.deleteById(id).awaitSuspending()
  }
}
