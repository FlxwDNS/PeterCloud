package dev.easycloud.terminal.command

class TestCommand: Command("test") {

    init {
        setExecutor {
            println("Test command executed successfully!")
        }

        val valueArgument = ArgumentType("value", String.javaClass)
        val enabledArgument = ArgumentType("enabled", Boolean.javaClass)
        addSyntax({ args ->
            val value = args[valueArgument]
            val enabled = args[enabledArgument] as Boolean

            println("Test command executed with value: $value and enabled: $enabled")
        }, valueArgument, enabledArgument)
    }
}
