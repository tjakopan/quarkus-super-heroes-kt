package io.quarkus.sample.superheroes.villain

import io.quarkus.hibernate.reactive.panache.kotlin.PanacheCompanion
import io.quarkus.hibernate.reactive.panache.kotlin.PanacheEntity
import io.smallrye.mutiny.coroutines.awaitSuspending
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive
import javax.validation.constraints.Size
import kotlin.random.Random

@Entity
class Villain : PanacheEntity() {
  @NotNull
  @Size(min = 3, max = 50)
  var name: String? = null

  var otherName: String? = null

  @NotNull
  @Positive
  var level: Int? = null

  var picture: String? = null

  @Column(columnDefinition = "TEXT")
  var powers: String? = null

  companion object : PanacheCompanion<Villain> {
    suspend fun findRandom(): Villain? {
      val countVillains = count().awaitSuspending()
      if (countVillains > 0) {
        val randomVillain = Random.nextInt(countVillains.toInt())
        return findAll().page(randomVillain, 1).firstResult().awaitSuspending()
      }
      return null
    }

    suspend fun listAllWhereNameLike(name: String?): List<Villain> =
      if (name != null) list("lower(name) like concat('%', ?1, '%')", name.lowercase()).awaitSuspending()
      else listOf()
  }

  override fun toString(): String {
    return "Villain(name='$name', otherName=$otherName, level=$level, picture=$picture, powers=$powers) ${super.toString()}"
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other == null || this::class != other::class) return false
    other as Villain
    return id == other.id
  }

  override fun hashCode(): Int = Objects.hash(id)
}
