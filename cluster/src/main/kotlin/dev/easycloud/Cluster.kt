package dev.easycloud

import dev.easycloud.grpc.GrpcService
import dev.easycloud.localisation.Localisation
import dev.easycloud.terminal.JLineTerminal
import dev.easycloud.terminal.logger.ClusterLogger
import dev.easycloud.yaml.ClusterYaml
import kotlinx.serialization.decodeFromString
import net.mamoe.yamlkt.Yaml
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText
import kotlin.system.exitProcess

val terminal = JLineTerminal()
val logger = ClusterLogger(terminal)

var localisation: Localisation? = null
lateinit var configuration: ClusterYaml

class Cluster {
    private val yamlPath: Path = Paths.get("cluster.yml")

    lateinit var grpcServer: GrpcService

    fun load() {
        val yaml = Yaml{

        }
        // Writing default configuration to cluster.toml if it does not exist
        if(!yamlPath.exists()) {
            yaml.encodeToString(ClusterYaml.serializer(), ClusterYaml()).let { yaml ->
                yamlPath.writeText(yaml)
            }
        }
        configuration = yaml.decodeFromString(yamlPath.readText())
        if(configuration.debug) {
            System.setProperty("debug", "true")
        }

        localisation = Localisation()

        if(System.getProperty("version").contains("pre")) {
            logger.warn("cluster.preReleaseWarning")
        }

        logger.info("cluster.loading")

        grpcServer = GrpcService()
    }

    fun run() {
        grpcServer.run()
        terminal.run()

        logger.info("cluster.running")

        // debug
        Thread.currentThread().join()
    }

    fun shutdown() {
        println()
        logger.info("cluster.shuttingDown")

        // Perform any necessary cleanup here
        grpcServer.shutdown()
        terminal.shutdown()

        logger.info("cluster.shutdownComplete")
        exitProcess(0)
    }
}