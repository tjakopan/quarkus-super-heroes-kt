package io.quarkus.sample.superheroes.villain.rest

import io.quarkus.qute.CheckedTemplate
import io.quarkus.qute.TemplateInstance
import io.quarkus.sample.superheroes.villain.Villain
import io.quarkus.sample.superheroes.villain.service.VillainService
import io.smallrye.common.annotation.Blocking
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType

@Path("/")
class UIResource(private val service: VillainService) {
  @CheckedTemplate
  object Templates {
    external fun index(villains: List<Villain>): TemplateInstance
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Blocking
  fun get(@QueryParam("name_filter") nameFilter: String?): TemplateInstance {
    val villains = if (nameFilter != null) service.findAllVillainsHavingName(nameFilter).await().indefinitely()
    else service.findAllVillains().await().indefinitely()
    return Templates.index(villains)
  }
}
