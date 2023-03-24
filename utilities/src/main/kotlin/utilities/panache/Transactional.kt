package utilities.panache

import io.quarkus.hibernate.reactive.panache.Panache
import io.smallrye.mutiny.coroutines.asUni
import io.smallrye.mutiny.coroutines.awaitSuspending
import kotlinx.coroutines.*

@OptIn(ExperimentalCoroutinesApi::class)
suspend inline fun <T> withTransaction(crossinline work: suspend CoroutineScope.() -> T): T = coroutineScope {
  Panache.withTransaction { async { work() }.asUni() }.awaitSuspending()
}
