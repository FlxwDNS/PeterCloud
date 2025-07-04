package dev.easycloud.terminal.command

class TestCommand: Command("test") {

    init {
        this.setExecutor {
            println("Test command executed successfully!")
        }

        this.addSyntax({ args ->
            println("Test command with arguments: $args")
        }, listOf(ArgumentType(), ArgumentType()))
    }
}