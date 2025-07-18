package dev.easycloud.terminal.command

import dev.easycloud.group.command.GroupCommand
import dev.easycloud.group.command.GroupsCommand
import dev.easycloud.logger

class CommandService {
    val commands: List<Command> = listOf(GroupCommand(), GroupsCommand(this), TestCommand())

    fun run(context: String) {
        val args = context.split(" ").toTypedArray()
        for (command in commands) {
            if (command.name.lowercase() == args[0].lowercase() || command.syntax.any { it.lowercase().equals(args[0].lowercase(), ignoreCase = true) }) {
                command.execute(args.drop(1).filter { it.isNotBlank() }.toTypedArray())
                return
            }
        }
        logger.error("command.notFound", context)
    }
}