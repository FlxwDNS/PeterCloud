package dev.easycloud

import com.akuleshov7.ktoml.file.TomlFileWriter
import dev.easycloud.terminal.JLineTerminal
import dev.easycloud.terminal.command.CommandService
import dev.easycloud.terminal.logger.ClusterLogger
import dev.easycloud.toml.ClusterToml
import kotlinx.serialization.serializer
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.exists

val logger = ClusterLogger()

class Cluster {
    val clusterToml: Path = Paths.get("cluster.toml")

    lateinit var terminal: JLineTerminal;

    fun load() {
        logger.info("»")
        logger.info("<gray>easycloud<white>@<blue>${System.getProperty("version")}<white>:<gray> ")

        // Writing default configuration to cluster.toml if it does not exist
        clusterToml.takeIf { !it.exists() }.apply {
            TomlFileWriter().encodeToFile(serializer(), ClusterToml(), clusterToml.toString())
        }

        this.terminal = JLineTerminal()

        logger.info("Loading cluster...")
    }

    fun run() {
        this.terminal.start()

        logger.info("Running cluster...")
    }
}