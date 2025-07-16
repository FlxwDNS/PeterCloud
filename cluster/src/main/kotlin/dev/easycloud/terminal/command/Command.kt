package dev.easycloud.terminal.command

import dev.easycloud.logger
import java.util.function.Consumer

abstract class Command(val name: String, val syntax: List<String> = emptyList()) {
    val syntaxes: MutableMap<List<ArgumentType>, Consumer<Map<ArgumentType, Any>>> = mutableMapOf()

    fun execute(context: Array<String>) {
        var syntax = syntaxes.filter { it.key.size == context.size }.map { it.value }.firstOrNull()
        if(syntax == null) {
            syntax = syntaxes[emptyList()]
            if(syntax == null) {
                logger.error("Please provide an valid executor.")
                return
            }
            syntax.accept(emptyMap())
            return
        }

        val arguments = mutableMapOf<ArgumentType, Any>()
        val syntaxArguments = syntaxes.filter { it.key.size == context.size }.map { it.key }.firstOrNull()
        if(syntaxArguments == null) {
            throw RuntimeException("No syntax arguments found for the provided context size: ${context.size}")
        }

        for (i in context.indices) {
            val value = context[i]
            val syntaxArgument = syntaxArguments[i]

            if(syntaxArgument.type == String.javaClass) {
                arguments[syntaxArgument] = value
            } else if(syntaxArgument.type == Boolean.javaClass) {
                if(value.toBooleanStrictOrNull() != null) {
                    arguments[syntaxArgument] = value.toBooleanStrict()
                } else {
                    logger.error("command.errorInvalidValue", "boolean", "'${syntaxArgument.id}'")
                    return
                }
                continue
            } else if(syntaxArgument.type == Int.javaClass) {
                if(value.toIntOrNull() != null) {
                    arguments[syntaxArgument] = value.toInt()
                } else {
                    logger.error("command.errorInvalidValue", "number", "'${syntaxArgument.id}'")
                    return
                }
            } else {
                logger.error("command.errorUnsupportedType", "${syntaxArgument.id}: ${syntaxArgument.type.simpleName}")
                return
            }
        }
        syntax.accept(arguments)
    }

    fun setExecutor(runnable: Runnable) {
        val context: Consumer<Map<ArgumentType, Any>> = Consumer { _ -> runnable.run() }
        addSyntax(context)
    }

    fun addSyntax(context: Consumer<Map<ArgumentType, Any>>, vararg arguments: ArgumentType) {
        syntaxes[arguments.asList()] = context
    }
}