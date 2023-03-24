plugins {
  kotlin("kapt")
  id("quarkus-conventions")
}

dependencies {
  implementation(project(":utilities"))
  implementation("io.quarkus:quarkus-resteasy-reactive-jackson")
  implementation("io.quarkus:quarkus-resteasy-reactive-kotlin")
  implementation("io.quarkus:quarkus-hibernate-reactive-panache-kotlin")
  implementation("io.quarkus:quarkus-hibernate-validator")
  implementation("io.quarkus:quarkus-reactive-pg-client")
  implementation("io.quarkus:quarkus-smallrye-openapi")
  implementation("io.quarkus:quarkus-smallrye-health")
  implementation("io.quarkus:quarkus-resteasy-reactive-qute")
  implementation("org.mapstruct:mapstruct:1.5.3.Final")
  kapt("org.mapstruct:mapstruct-processor:1.5.3.Final")
  implementation("io.vertx:vertx-lang-kotlin-coroutines")

  testImplementation("io.quarkus:quarkus-jacoco")
  testImplementation("io.quarkus:quarkus-junit5-mockito")
  testImplementation("io.quarkus:quarkus-panache-mock")
  testImplementation("io.rest-assured:rest-assured")
  testImplementation("io.rest-assured:kotlin-extensions")
  testCompileOnly("io.quarkiverse.pact:quarkus-pact-provider:0.2.1")
}
