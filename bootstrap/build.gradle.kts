import java.security.MessageDigest

plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
    maven(url = "https://s01.oss.sonatype.org/content/repositories/snapshots/")
    maven(url = "https://oss.sonatype.org/content/repositories/snapshots/")
}

val download = configurations.create("download")
dependencies {
    // jline
    download("org.jline:jline:3.30.4")

    // toml
    //download("com.akuleshov7:ktoml-core:0.7.0")
    //download("com.akuleshov7:ktoml-file:0.7.0")

    // yaml
    //download("com.charleskorn.kaml:kaml:0.83.0")
    download("net.mamoe.yamlkt:yamlkt:0.13.0")

    // grpc
    download("io.grpc:grpc-protobuf:1.73.0")
    download("io.grpc:grpc-services:1.73.0")
    download("io.grpc:grpc-netty:1.73.0")
    download("io.grpc:grpc-kotlin-stub:1.4.3")
}

val outputFile = layout.buildDirectory.file("download-dependencies.txt")
val exportDownloadDependencies = tasks.register("exportDownloadDependencies") {
    group = "build"
    description = "Exports the download dependencies to a file."

    doLast {
        val resolvedArtifacts = download.resolvedConfiguration.resolvedArtifacts
        val output = outputFile.get().asFile
        val mavenRepos = repositories.filterIsInstance<MavenArtifactRepository>()

        output.printWriter().use { writer ->
            download.resolve().forEach { file ->
                val artifact = resolvedArtifacts.find { it.file.name == file.name }
                if (artifact != null) {
                    val group = artifact.moduleVersion.id.group
                    val name = artifact.name
                    val version = artifact.moduleVersion.id.version
                    val sha256 = file.inputStream().use { stream ->
                        val buffer = ByteArray(8192)
                        val digest = MessageDigest.getInstance("SHA-256")
                        var bytesRead: Int
                        while (stream.read(buffer).also { bytesRead = it } != -1) {
                            digest.update(buffer, 0, bytesRead)
                        }
                        digest.digest().joinToString("") { "%02x".format(it) }
                    }

                    val relativePath = "${group.replace('.', '/')}/$name/$version/$name-$version.jar"
                    val url = mavenRepos.firstOrNull()?.url?.toString()?.trimEnd('/') + "/" + relativePath

                    writer.println("$group:$name:$version:$sha256:${url.replace("https://", "")}")
                }
            }
        }
    }
}

tasks.withType<Jar> {
    dependsOn(exportDownloadDependencies)

    from(outputFile) {
        into("")
        rename { "cloud.dependencies" }
    }

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    from(project(":grpc").tasks.jar)
    from(project(":cluster").tasks.jar)

    manifest {
        attributes["Main-Class"] = "dev.easycloud.BootstrapBoot"
        attributes["project-version"] = version
    }

    archiveFileName.set("bootstrap.jar")
}

tasks.register("runBootstrap") {
    group = "build"
    description = "Builds the jar and runs the bootstrap in a separate terminal."

    dependsOn("jar")

    doLast {
        val jarTask = tasks.named("jar").get() as Jar
        val jarFile = jarTask.archiveFile.get().asFile
        val outputDir = File("C:\\Users\\Radik\\Desktop\\EasyCloudV3")

        copy {
            from(jarFile)
            from("start.bat")
            into(outputDir)
        }

        val batPath = File(outputDir, "start.bat").absolutePath

        ProcessBuilder("cmd", "/c", "start", "", "cmd", "/c", batPath)
            .directory(outputDir)
            .start()

        println("Bootstrap launched. Check the terminal for output.")
    }
}
