package dev.easycloud.terminal.command

class TestCommand: Command("test") {

    init {
        setExecutor {
            println("Test command executed successfully!")
        }

        addSyntax({ args ->
            println("Test command with arguments: $args")
        }, listOf(ArgumentType(), ArgumentType()))
    }
}