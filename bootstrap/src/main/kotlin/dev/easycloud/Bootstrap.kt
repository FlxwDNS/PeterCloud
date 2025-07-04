package dev.easycloud

import dev.easycloud.dependency.DependencyLoader
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.util.Locale
import kotlin.io.path.*
import kotlin.system.exitProcess

class Bootstrap {
    @OptIn(ExperimentalPathApi::class)
    fun load() {
        val localPath = Paths.get("local")
        val dependenciesPath = Paths.get("dependencies")
        val librariesPath = dependenciesPath.resolve("libraries")

        localPath.takeIf { !it.exists() }?.createDirectory()
        dependenciesPath.takeIf { !it.exists() }?.createDirectory()
        librariesPath.takeIf { !it.exists() }?.createDirectory()

        // Remove old dynamic directory if it exists
        localPath.resolve("dynamic").deleteRecursively()

        // Check if the loader-patcher.jar file exists
        if (Files.exists(Paths.get("loader-patcher.jar"))) {
            println("Patching could have been failed.")
            Files.delete(Paths.get("loader-patcher.jar"))
        }

        // Load the EasyCloudCluster
        mapOf(
            Pair("grpc", librariesPath.resolve("grpc")),
            Pair("cluster", librariesPath.resolve("cluster")),
        ).forEach {
            this.copyFile("${it.key}.jar", Paths.get("${it.value}.jar"))
        }

        DependencyLoader().load(dependenciesPath)
    }

    fun run() {
        val thread = Thread {
            try {
                var fileArg = "dependencies/libraries/cluster.jar;dependencies/*;"
                if (!System.getProperty("os.name").lowercase(Locale.getDefault()).contains("win")) {
                    fileArg = fileArg.replace(";", ":")
                }
                val process = ProcessBuilder("java","-cp", fileArg, "dev.easycloud.ClusterBootKt")
                    .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                    .redirectError(ProcessBuilder.Redirect.INHERIT)
                    .redirectInput(ProcessBuilder.Redirect.INHERIT)
                    .start()
                process.waitFor()
            } catch (exception: IOException) {
                throw RuntimeException(exception)
            } catch (_: InterruptedException) {
                Thread.currentThread().interrupt()
                exitProcess(1)
            }
        }
        thread.name = "Cluster"
        thread.setDaemon(false)
        thread.start()

        Runtime.getRuntime().addShutdownHook(Thread { if (thread.isAlive) thread.interrupt() })

        while (true) {
            if (!thread.isAlive) exitProcess(0)
        }
    }

    private fun copyFile(name: String, destination: Path) {
        val file = ClassLoader.getSystemClassLoader().getResourceAsStream(name) ?: throw IllegalArgumentException("Resource $name not found")
        Files.copy(file, destination, StandardCopyOption.REPLACE_EXISTING)
    }
}
