package dev.easycloud.localisation

import dev.easycloud.Cluster
import java.util.Locale
import java.util.ResourceBundle

class Localisation(cluster: Cluster) {
    val locale: Locale = Locale.of(cluster.clusterYaml.locale)
    val resourceBundle: ResourceBundle = ResourceBundle.getBundle("i18n", locale)

    fun get(key: String, vararg args: Any?): String {
        return try {
            String.format(locale, resourceBundle.getString(key), *args)
        } catch (e: Exception) {
            // Fallback to the key itself if the key is not found
            "localisation not found: '$key'"
        }
    }
}