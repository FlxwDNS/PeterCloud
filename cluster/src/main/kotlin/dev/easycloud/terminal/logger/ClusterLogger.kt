package dev.easycloud.terminal.logger

import dev.easycloud.localisation
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Suppress("unused")
class ClusterLogger {
    fun info(message: String, vararg args: Any) {
        println(format("info", message, args))
    }

    fun warn(message: String, vararg args: Any) {
        println(format("warn", message, args))
    }

    fun error(message: String, vararg args: Any) {
        println(format("error", message, args))
    }

    fun debug(message: String, vararg args: Any) {
        println(format("debug", message, args))
    }

    fun trace(message: String, vararg args: Any) {
        println(format("trace", message, args))
    }

    fun colorText(message: String, vararg args: Any): String {
        return Color.entries.fold(message) { it, color ->
            it.replace("<" + color.name.lowercase() + ">", color.ascii)
        }.format(*args)
    }

    fun format(type: String, message: String, vararg args: Any): String {
        val time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
        return "[$time] ${type.uppercase()}: ${colorText(localisation?.get(message, args) ?: "localisation is null", *args)}"
    }
}

enum class Color(val ascii: String) {
    RESET("\u001B[0m"),
    BLACK("\u001B[30m"),
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    BLUE("\u001B[94m"),
    PURPLE("\u001B[35m"),
    CYAN("\u001B[36m"),
    GRAY("\u001B[37m"),
    WHITE("\u001B[97m");
}