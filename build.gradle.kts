plugins {
    kotlin("jvm") version "2.1.21"
}

allprojects {
    apply(plugin = "java-library")

    group = "dev.easycloud"
    version = "3.0-pre1"

    repositories {
        mavenCentral()
        maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
    }

    dependencies {
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