package utilities.service

import io.smallrye.mutiny.Uni
import kotlinx.coroutines.*

inline fun <T> CoroutineScope.callService(crossinline function: suspend CoroutineScope.() -> T): Uni<T> =
  Uni.createFrom().emitter { em ->
    // Usually called from supervisor scope, so failure of this job won't cause failure of parent supervisor
    // or supervisor's children jobs.
    launch {
      // Creates new scope which overrides supervisor job, so any children od this scope will cause this scope to
      // be cancelled and this scope's children.
      coroutineScope {
        try {
          val result = function()
          em.complete(result)
        } catch (e: Throwable) {
          em.fail(e)
        }
        em.onTermination { if (isActive) cancel() }
      }
    }
  }

inline fun CoroutineScope.runService(crossinline function: suspend CoroutineScope.() -> Unit): Uni<Void> =
  Uni.createFrom().emitter { em ->
    launch {
      coroutineScope {
        try {
          function()
          em.complete(null)
        } catch (e: Throwable) {
          em.fail(e)
        }
        em.onTermination { if (isActive) cancel() }
      }
    }
  }
