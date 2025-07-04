package dev.easycloud

import com.akuleshov7.ktoml.file.TomlFileReader
import com.akuleshov7.ktoml.file.TomlFileWriter
import dev.easycloud.toml.ClusterToml
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.serializer
import java.nio.file.Paths
import kotlin.io.path.exists
import kotlin.io.path.readText

private val logger = KotlinLogging.logger {  }
class Cluster {
    val clusterToml = Paths.get("cluster.toml")

    fun load() {
        // Writing default configuration to cluster.toml if it does not exist
        clusterToml.takeIf { !it.exists() }.apply {
            TomlFileWriter().encodeToFile(serializer(), ClusterToml(), clusterToml.toString())
        }

       // logger.info { Toml().encodeToString(serializer(), clusterToml.toString()) }

        val config = TomlFileReader.decodeFromString(ClusterToml.serializer(), clusterToml.readText())

        logger.info { config.network.host }

        logger.info { "Loading cluster..." }
    }

    fun run() {
        logger.info { "Running cluster..." }
    }
}