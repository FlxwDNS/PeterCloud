plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "2.2.0"
}

dependencies {
    compileOnly(project(":grpc"))

    compileOnly("com.akuleshov7:ktoml-core:0.7.0")
    compileOnly("com.akuleshov7:ktoml-file:0.7.0")
}

tasks.withType<Jar> {
    archiveFileName.set("cluster.jar")
}