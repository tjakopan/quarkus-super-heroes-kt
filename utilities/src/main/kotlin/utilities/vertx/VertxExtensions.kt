package utilities.vertx

import io.vertx.kotlin.coroutines.dispatcher
import io.vertx.mutiny.core.Vertx

fun Vertx.dispatcher() = delegate.dispatcher()
