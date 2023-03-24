plugins {
  `kotlin-dsl`
}

repositories {
  gradlePluginPortal()
}

dependencies {
  implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.10")
  implementation("org.jetbrains.kotlin:kotlin-allopen:1.8.10")
  implementation("io.quarkus:gradle-application-plugin:2.16.4.Final")
}
