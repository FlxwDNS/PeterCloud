package dev.easycloud.localisation

import dev.easycloud.configuration
import java.util.Locale
import java.util.ResourceBundle

class Localisation() {
    val locale: Locale = Locale.of(configuration.locale)
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