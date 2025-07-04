plugins {
    kotlin("jvm") version "2.1.21"
}

allprojects {
    apply(plugin = "java-library")

    group = "dev.easycloud"
    version = "1.0-SNAPSHOT"

    repositories {
      mavenCentral()
    }

    dependencies {
        // logging:kotlin
        "compileOnly"("io.github.oshai:kotlin-logging:7.0.7")
        // logging:api
        "compileOnly"("org.slf4j:slf4j-api:2.0.17")
        // logging:provider
        "compileOnly"("org.slf4j:slf4j-log4j12:2.0.17")
    }

    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        sourceCompatibility = JavaVersion.VERSION_21.toString()
        targetCompatibility = JavaVersion.VERSION_21.toString()
    }
}

kotlin {
    jvmToolchain(21)
}