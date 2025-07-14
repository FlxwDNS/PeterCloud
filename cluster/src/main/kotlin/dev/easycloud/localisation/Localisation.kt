package dev.easycloud.localisation

import dev.easycloud.Cluster
import java.util.Locale
import java.util.ResourceBundle

class Localisation(cluster: Cluster) {
    val resourceBundle = ResourceBundle.getBundle("i18n", Locale.of(cluster.clusterToml.locale))


    fun get(key: String, vararg args: Any): String {
        return try {
            String.format(resourceBundle.getString(key), *args)
        } catch (e: Exception) {
            // Fallback to the key itself if the key is not found
            key
        }
    }
}