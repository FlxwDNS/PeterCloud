package dev.easycloud.toml

import com.akuleshov7.ktoml.annotations.TomlComments
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

@Serializable
data class ClusterToml(
    @TomlComments("You can modify this file to change the cluster settings.", "Use 'reload' command in console to apply changes.")
    val tomlVersion: String = "1.0.0",

    @TomlComments("Language used for messages in the cluster. Default is English (en_US).")
    val locale: String = "en_US",

    val network: NetworkToml = NetworkToml(),
    @SerialName("group")
    val groups: Map<String, GroupToml> = mapOf(
        Pair("proxy", GroupToml("velocity_1.3.0",
            mapOf(
                Pair("java", "java"),
                Pair("fallback", "false"),
                Pair("saveFiles", "false"),
                Pair("memory", "512"),
                Pair("alwaysOnline", "1"),
                Pair("maxOnline", "1"),
                Pair("maxPlayers", "787"),
            )
        )),
        Pair("lobby", GroupToml("paper_1.21.7",
            mapOf(
                Pair("java", "java"),
                Pair("fallback", "true"),
                Pair("saveFiles", "false"),
                Pair("memory", "2048"),
                Pair("alwaysOnline", "1"),
                Pair("maxOnline", "-1"),
                Pair("maxPlayers", "10"),
            )
        )),
    ),
)

@Serializable
data class NetworkToml(
    val host: String = "127.0.0.1",
    val port: Int = 31415,
)

@Serializable
data class GroupToml(
    val platform: String,
    @TomlComments("Properties of an group. You can also add custom properties here for later use.")
    val properties: Map<String, String>,
)