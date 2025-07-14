package dev.easycloud

import com.akuleshov7.ktoml.Toml
import com.akuleshov7.ktoml.TomlInputConfig
import dev.easycloud.grpc.GrpcService
import dev.easycloud.localisation.Localisation
import dev.easycloud.terminal.JLineTerminal
import dev.easycloud.terminal.logger.ClusterLogger
import dev.easycloud.toml.ClusterToml
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText
import kotlin.system.exitProcess

val logger = ClusterLogger()
var localisation: Localisation? = null

class Cluster {
    private val toml = Toml(
        inputConfig = TomlInputConfig(
            ignoreUnknownNames = true
        )
    )
    private val tomlPath: Path = Paths.get("cluster.toml")

    lateinit var clusterToml: ClusterToml
    lateinit var terminal: JLineTerminal
    lateinit var grpcServer: GrpcService

    fun load() {

        // Writing default configuration to cluster.toml if it does not exist
        if(!tomlPath.exists()) {
            tomlPath.writeText(toml.encodeToString<ClusterToml>(ClusterToml()))
        }
        clusterToml = toml.decodeFromString<ClusterToml>(tomlPath.readText())
        if(clusterToml.debug) {
            System.setProperty("debug", "true")
        }

        localisation = Localisation(this)

        logger.info("cluster.loading")

        terminal = JLineTerminal()
        logger.debug("debug.terminalInitialized")

        grpcServer = GrpcService()
    }

    fun run() {
        grpcServer.run(this)

        terminal.run()

        logger.info("cluster.running")
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