import com.google.protobuf.gradle.id

plugins {
    kotlin("jvm")
    id("com.google.protobuf") version "0.9.5"
}

dependencies {
    implementation("io.grpc:grpc-protobuf:1.73.0")
    implementation("io.grpc:grpc-services:1.73.0")
    implementation("io.grpc:grpc-netty:1.73.0")
    implementation("io.grpc:grpc-kotlin-stub:1.4.3")
    implementation("com.google.protobuf:protobuf-kotlin:4.31.1")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:4.31.1"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.73.0"
        }
    }
    generateProtoTasks {
        ofSourceSet("main").forEach {
            it.plugins {
                id("grpc") {}
            }
        }
    }
}

tasks.withType<Jar> {
    archiveFileName.set("grpc.jar")
}
