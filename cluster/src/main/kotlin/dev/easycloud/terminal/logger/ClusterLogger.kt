package dev.easycloud.terminal.logger

@Suppress("unused")
class ClusterLogger {
    fun info(message: String) {
        println(this.format("info", message))
    }

    fun warn(message: String) {
        println(this.format("warn", message))
    }

    fun error(message: String) {
        println(this.format("error", message))
    }

    fun debug(message: String) {
        println(this.format("debug", message))
    }

    fun trace(message: String) {
        println(this.format("trace", message))
    }

    fun colorText(message: String, vararg args: Any): String {
        return Color.entries.fold(message) { it, color ->
            it.replace("<" + color.name.lowercase() + ">", color.ascii)
        }.format(*args)
    }

    fun format(type: String, message: String, vararg args: Any): String {
        return "[${type}] ${colorText(message, *args)}"
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