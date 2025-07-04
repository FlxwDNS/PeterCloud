package dev.easycloud.terminal.command

class CommandService {
    val commands: List<Command> = listOf(TestCommand())

    fun run(context: String) {
        val args = context.split(" ").toTypedArray()
        for (command in commands) {
            if (command.name.lowercase() == args[0].lowercase() || command.syntax.any { it.lowercase().equals(args[0].lowercase(), ignoreCase = true) }) {
                command.execute(args.drop(1).toTypedArray())
                return
            }
        }
        println("Command not found: $context")
    }
}