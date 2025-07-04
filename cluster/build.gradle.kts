plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "2.2.0"
}

dependencies {
    compileOnly(project(":grpc"))

    // jline
    compileOnly("org.jline:jline:3.30.4")

    // ktoml
    compileOnly("com.akuleshov7:ktoml-core:0.7.0")
    compileOnly("com.akuleshov7:ktoml-file:0.7.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
}

tasks.withType<Jar> {
    archiveFileName.set("cluster.jar")
}