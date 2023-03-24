package io.quarkus.sample.superheroes.villain

import io.kotest.matchers.longs.shouldBeZero
import io.kotest.matchers.nulls.shouldBeNull
import io.quarkus.test.TestTransaction
import io.quarkus.test.junit.QuarkusTest
import io.smallrye.mutiny.coroutines.awaitSuspending
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

private const val DEFAULT_NAME = "Super Chocolatine"
private const val DEFAULT_OTHER_NAME = "Super Chocolatine chocolate in"
private const val DEFAULT_PICTURE = "super_chocolatine.png"
private const val DEFAULT_POWERS = "does not eat pain au chocolat"
private const val DEFAULT_LEVEL = 42

@OptIn(ExperimentalCoroutinesApi::class)
@QuarkusTest
@TestTransaction
class VillainTest {
  @Test
  fun `findRandom not found`() = runTest {
//    Villain.deleteAll().awaitSuspending()
//
//    Villain.count().awaitSuspending().shouldBeZero()
//    Villain.findRandom().shouldBeNull()
  }
}
