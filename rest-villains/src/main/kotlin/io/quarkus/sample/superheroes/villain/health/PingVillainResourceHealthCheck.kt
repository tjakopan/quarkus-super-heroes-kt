package io.quarkus.sample.superheroes.villain.health

import io.quarkus.sample.superheroes.villain.rest.VillainResource
import org.eclipse.microprofile.health.HealthCheck
import org.eclipse.microprofile.health.HealthCheckResponse
import org.eclipse.microprofile.health.Liveness

@Liveness
class PingVillainResourceHealthCheck(private val villainResource: VillainResource) : HealthCheck {
  override fun call(): HealthCheckResponse {
    val response = villainResource.hello()
    return HealthCheckResponse.named("Ping Villain REST Endpoint")
      .withData("Response", response)
      .up()
      .build()
  }
}
