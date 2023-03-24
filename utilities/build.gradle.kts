plugins {
  id("quarkus-conventions")
  `java-library`
}

dependencies {
  compileOnly("io.quarkus:quarkus-hibernate-reactive-panache")
  compileOnly("io.quarkus:quarkus-hibernate-reactive-panache-kotlin")
  compileOnly("io.quarkus:quarkus-cache")
  compileOnly("io.vertx:vertx-lang-kotlin-coroutines")
}
