package io.quarkus.sample.superheroes.villain.rest

import io.quarkus.sample.superheroes.villain.Villain
import io.quarkus.sample.superheroes.villain.service.VillainService
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType
import org.eclipse.microprofile.openapi.annotations.headers.Header
import org.eclipse.microprofile.openapi.annotations.media.Content
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.tags.Tag
import org.jboss.logging.Logger
import java.net.URI
import javax.validation.Valid
import javax.validation.constraints.NotNull
import javax.ws.rs.*
import javax.ws.rs.core.*
import javax.ws.rs.core.Response.Status

@Path("/api/villains")
@Tag(name = "villains")
@Produces(MediaType.APPLICATION_JSON)
class VillainResource(private val logger: Logger, private val service: VillainService) {
  @GET
  @Path("/random")
  @Operation(summary = "Returns a random villain")
  @APIResponse(
    responseCode = "200",
    description = "Gets random villain",
    content = [Content(
      mediaType = MediaType.APPLICATION_JSON,
      schema = Schema(implementation = Villain::class, required = true)
    )]
  )
  @APIResponse(responseCode = "404", description = "No villain found")
  suspend fun getRandomVillain(): Response {
    val villain = service.findRandomVillain().awaitSuspending()
    if (villain != null) {
      logger.debug("Found random villain: $villain")
      return Response.ok(villain).build()
    }
    logger.debug("No random villain found")
    return Response.status(Status.NOT_FOUND).build()
  }

  @GET
  @Operation(summary = "Returns all the villains from the database")
  @APIResponse(
    responseCode = "200",
    description = "Get all villains",
    content = [Content(
      mediaType = MediaType.APPLICATION_JSON,
      schema = Schema(implementation = Villain::class, type = SchemaType.ARRAY)
    )]
  )
  suspend fun getAllVillains(
    @Parameter(
      name = "name_filter",
      description = "An optional filter parameter to filter results by name"
    ) @QueryParam("name_filter") nameFilter: String?
  ): List<Villain> {
    val villains = if (nameFilter != null) service.findAllVillainsHavingName(nameFilter).awaitSuspending()
    else service.findAllVillains().awaitSuspending()
    logger.debug("Total number of villains: ${villains.size}")
    return villains
  }

  @GET
  @Path("/{id}")
  @Operation(summary = "Returns a villain for a given identifier")
  @APIResponse(
    responseCode = "200",
    description = "Gets a villain for a given id",
    content = [Content(mediaType = MediaType.APPLICATION_JSON, schema = Schema(implementation = Villain::class))]
  )
  @APIResponse(responseCode = "404", description = "The villain is not found for a given identifier")
  suspend fun getVillain(@Parameter(name = "id", required = true) @PathParam("id") id: Long): Response {
    val villain = service.findVillainById(id).awaitSuspending()
    if (villain != null) {
      logger.debug("Found villain: $villain")
      return Response.ok(villain).build()
    }
    logger.debug("No villain found with id $id")
    return Response.status(Status.NOT_FOUND).build()
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(summary = "Creates a valid villain")
  @APIResponse(
    responseCode = "201",
    description = "The URI of the created villain",
    headers = [Header(name = HttpHeaders.LOCATION, schema = Schema(implementation = URI::class))]
  )
  @APIResponse(responseCode = "404", description = "Invalid villain passed in (or no request body found)")
  suspend fun createVillain(@Valid @NotNull villain: Villain, @Context uriInfo: UriInfo): Response {
    val v = service.persistVillain(villain).awaitSuspending()
    val uri = uriInfo.absolutePathBuilder.path(v.id.toString()).build()
    logger.debug("New villain created with URI $uri")
    return Response.created(uri).build()
  }

  @PUT
  @Path("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(summary = "Completely updates/replaces an existing villain by replacing it with the passed-in villain")
  @APIResponse(responseCode = "204", description = "Replaced the villain")
  @APIResponse(responseCode = "400", description = "Invalid villain passed in (or no request body found)")
  @APIResponse(responseCode = "404", description = "No villain found")
  suspend fun fullyUpdateVillain(@Valid @NotNull villain: Villain): Response {
    val v = service.replaceVillain(villain).awaitSuspending()
    if (v != null) {
      logger.debug("Villain replaced with new values $v")
      return Response.noContent().build();
    }
    logger.debug("No villain found with id ${villain.id}")
    return Response.status(Status.NOT_FOUND).build()
  }

  @PUT
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(summary = "Completely replace all villains with the passed-in villains")
  @APIResponse(
    responseCode = "201",
    description = "The URI to retrieve all the created villains",
    headers = [Header(name = HttpHeaders.LOCATION, schema = Schema(implementation = URI::class))]
  )
  @APIResponse(responseCode = "400", description = "Invalid villains passed in (or no request body found)")
  suspend fun replaceAllVillains(@NotNull villains: List<Villain>, @Context uriInfo: UriInfo): Response {
    service.replaceAllVillains(villains).awaitSuspending()
    var uri = uriInfo.absolutePathBuilder.build()
    logger.debug("New villains created with URI $uri")
    return Response.created(uri).build()
  }

  @PATCH
  @Path("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Operation(summary = "Partially updates an existing villain")
  @APIResponse(
    responseCode = "200",
    description = "Updated the villain",
    content = [Content(mediaType = MediaType.APPLICATION_JSON, schema = Schema(implementation = Villain::class))]
  )
  @APIResponse(responseCode = "400", description = "Null villain passed in (or no request body found)")
  @APIResponse(responseCode = "404", description = "No villain found")
  suspend fun partiallyUpdateVillain(
    @Parameter(name = "id", required = true) @PathParam("id") id: Long,
    @NotNull villain: Villain
  ): Response {
    if (villain.id == null) villain.id = id
    val v = service.partialUpdateVillain(villain).awaitSuspending()
    if (v != null) {
      logger.debug("Villain updated with new values $v")
      return Response.ok(v).build()
    }
    logger.debug("No villain found with id ${villain.id}")
    return Response.status(Status.NOT_FOUND).build()
  }

  @DELETE
  @Operation(summary = "Delete all villains")
  @APIResponse(responseCode = "204", description = "Deletes all villains")
  suspend fun deleteAllVillains() {
    service.deleteAllVillains().awaitSuspending()
    logger.debug("Deleted all villains")
  }

  @DELETE
  @Path("/{id}")
  @Operation(summary = "Deletes an existing villain")
  @APIResponse(responseCode = "204", description = "Delete a villain")
  suspend fun deleteVillain(@Parameter(name = "id", required = true) @PathParam("id") id: Long) {
    service.deleteVillain(id).awaitSuspending()
    logger.debug("Villain with id $id deleted")
  }

  @GET
  @Path("/hello")
  @Produces(MediaType.TEXT_PLAIN)
  @Tag(name = "hello")
  @Operation(summary = "Ping hello")
  @APIResponse(responseCode = "200", description = "Ping hello")
  fun hello(): String = "Hello villain resource"
}
