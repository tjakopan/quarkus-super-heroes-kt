package io.quarkus.sample.superheroes.villain.config

import io.smallrye.config.ConfigMapping
import io.smallrye.config.WithDefault

@ConfigMapping(prefix = "villain")
interface VillainConfig {
  fun level(): Level

  interface Level {
    @WithDefault("1.0")
    fun multiplier(): Double
  }
}
