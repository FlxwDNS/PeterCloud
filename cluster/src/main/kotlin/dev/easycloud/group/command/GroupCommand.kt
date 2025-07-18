package dev.easycloud.group.command

import dev.easycloud.logger
import dev.easycloud.terminal.command.ArgumentType
import dev.easycloud.terminal.command.Command

class GroupCommand: Command("group") {

    init {
        setExecutor {
            logger.info("Group command executed successfully!")
        }

        val nameArgument = ArgumentType("name", String.javaClass)

        // info syntax
        addSyntax({ args ->
            val name = args[nameArgument] as String
            logger.info("Group info command executed for group: $name")
        }, nameArgument)

        // create syntax
        val platformArgument = ArgumentType("platform", String.javaClass)
        val memoryArgument = ArgumentType("memory", Int.javaClass)
        val fallbackArgument = ArgumentType("fallback", Boolean.javaClass)
        addSyntax({ args ->
            val name = args[nameArgument] as String
            val platform = args[platformArgument] as String
            val memory = args[memoryArgument] as Int
            val fallback = args[fallbackArgument] as Boolean

            logger.info("Group create command executed for group: $name with platform: $platform, memory: $memory, fallback: $fallback")
        }, nameArgument, platformArgument, memoryArgument, fallbackArgument)
    }
}