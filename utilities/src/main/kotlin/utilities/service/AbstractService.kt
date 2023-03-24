package utilities.service

import io.vertx.mutiny.core.Vertx
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import utilities.vertx.dispatcher
import javax.annotation.PreDestroy
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

abstract class AbstractService : CoroutineScope, AutoCloseable {
  // Only here to make quarkus-arc happy, instead of being constructor injected. Otherwise, components that extend
  // this class fail with: It's not possible to automatically add a synthetic no-args constructor to an unproxyable
  // bean class.
  @Inject
  protected lateinit var vertx: Vertx

  override val coroutineContext: CoroutineContext by lazy { vertx.dispatcher() + SupervisorJob() }

  @PreDestroy
  override fun close() {
    coroutineContext.cancel()
  }
}
