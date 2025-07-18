package dev.easycloud.yaml

import dev.easycloud.group.Group
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.mamoe.yamlkt.Comment

@Serializable
data class ClusterYaml(
    @Comment("Language used for messages in the cluster. Default is English (en).")
    val locale: String = "en",

    @Comment("Debug mode. If enabled, the cluster will log more information. This is useful for better support and debugging.")
    val debug: Boolean = false,

    val network: NetworkYaml = NetworkYaml(),
    @SerialName("group")
    val groups: List<Group> = listOf(
        Group(
            "proxy", "velocity_1.3.0", mutableMapOf(
                //Pair("java", "java"),
                //Pair("fallback", "false"),
                //Pair("saveFiles", "false"),
                Pair("memory", "512"),
                Pair("alwaysOnline", "1"),
                Pair("maxOnline", "1"),
                Pair("maxPlayers", "787")
            )
        ),
        Group(
            "lobby", "paper_1.21.7", mutableMapOf(
                //Pair("java", "java"),
                Pair("fallback", "true"),
                //Pair("saveFiles", "false"),
                Pair("memory", "2048"),
                Pair("alwaysOnline", "1"),
                //Pair("maxOnline", "-1"),
                Pair("maxPlayers", "10")
            )
        ),
    ),
)

@Serializable
data class NetworkYaml(
    @Comment("Port where gRPC server will listen.")
    val port: Int = 2500,
)