package dev.easycloud.terminal.logger

import dev.easycloud.localisation
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Suppress("unused")
class ClusterLogger {
    fun info(message: String, vararg args: Any) {
        println(format("<white>INFO<reset>", message, *args))
    }

    fun warn(message: String, vararg args: Any) {
        println(format("<yellow>WARN<reset>", message, *args))
    }

    fun error(message: String, vararg args: Any) {
        println(format("<red>ERROR<reset>", message, *args))
    }

    fun debug(message: String, vararg args: Any) {
        if(System.getProperty("debug") != "true") return

        println(format("<yellow>DEBUG<reset>", message, *args))
    }

    fun trace(message: String, vararg args: Any) {
        println(format("TRACE", message, *args))
    }

    fun colorText(message: String): String {
        return Color.entries.fold(message) { it, color ->
            it.replace("<" + color.name.lowercase() + ">", color.ascii)
        }
    }

    fun format(type: String, message: String, vararg args: Any): String {
        val time = LocalDateTime.now().format(
            System.getProperty("debug")?.let {
                DateTimeFormatter.ofPattern("HH:mm:ss.SSS")
            } ?: DateTimeFormatter.ofPattern("HH:mm:ss")
        )
        return colorText("[<white>$time<reset>] ${type}: ${localisation?.get(message, *args) ?: "localisation is null"}")
    }
}

enum class Color(val ascii: String) {
    RESET("\u001B[0m"),
    BLACK("\u001B[30m"),
    RED("\u001B[91m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    BLUE("\u001B[94m"),
    PURPLE("\u001B[35m"),
    CYAN("\u001B[36m"),
    GRAY("\u001B[37m"),
    WHITE("\u001B[97m");
}