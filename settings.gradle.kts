plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "EasyCloud"
include("cluster")
include("bootstrap")
include("grpc")