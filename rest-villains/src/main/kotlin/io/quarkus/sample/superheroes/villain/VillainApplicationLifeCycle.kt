package io.quarkus.sample.superheroes.villain

import io.quarkus.runtime.ShutdownEvent
import io.quarkus.runtime.StartupEvent
import io.quarkus.runtime.configuration.ProfileManager
import org.jboss.logging.Logger
import javax.enterprise.context.ApplicationScoped
import javax.enterprise.event.Observes

@ApplicationScoped
object VillainApplicationLifeCycle {
  private val logger: Logger = Logger.getLogger(VillainApplicationLifeCycle::class.java)

  fun onStart(@Observes ev: StartupEvent) {
    logger.info("The application VILLAIN is starting with profile " + ProfileManager.getActiveProfile())
  }

  fun onStop(@Observes ev: ShutdownEvent) {
    logger.info("The application VILLAIN is stopping...")
  }
}
