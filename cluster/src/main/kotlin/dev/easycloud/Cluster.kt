package dev.easycloud

import com.akuleshov7.ktoml.file.TomlFileReader
import com.akuleshov7.ktoml.file.TomlFileWriter
import dev.easycloud.localisation.Localisation
import dev.easycloud.terminal.JLineTerminal
import dev.easycloud.terminal.logger.ClusterLogger
import dev.easycloud.toml.ClusterToml
import kotlinx.serialization.serializer
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.exists

val logger = ClusterLogger()
var localisation: Localisation? = null

class Cluster {
    private val tomlPath: Path = Paths.get("cluster.toml")

    lateinit var clusterToml: ClusterToml
    lateinit var terminal: JLineTerminal

    fun load() {
        // Writing default configuration to cluster.toml if it does not exist
        tomlPath.takeIf { !it.exists() }.apply {
            TomlFileWriter().encodeToFile(serializer(), ClusterToml(), tomlPath.toString())
        }
        clusterToml = TomlFileReader.decodeFromFile(serializer(), tomlPath.toString())

        localisation = Localisation(this)

        terminal = JLineTerminal()

        logger.info("Loading cluster...")
    }

    fun run() {
        terminal.run()

        logger.info("Running cluster...")
    }

    fun shutdown() {
        logger.info("Shutting down cluster...")
        // Perform any necessary cleanup here
        terminal.shutdown()
    }
}