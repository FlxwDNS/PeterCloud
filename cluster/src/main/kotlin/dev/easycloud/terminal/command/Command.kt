package dev.easycloud.terminal.command

import dev.easycloud.logger
import java.util.function.Consumer

abstract class Command(val name: String, val syntax: List<String> = emptyList()) {
    val syntaxes: MutableMap<List<ArgumentType>, Consumer<Map<ArgumentType, Any>>> = mutableMapOf()

    fun execute(context: Array<String>) {
        val arguments = mutableMapOf<ArgumentType, Any>()
        for (i in context.indices) {
            val argType = ArgumentType()
            arguments[argType] = context[i]
        }

        var syntax = syntaxes.filter { it.key.size == context.size }.map { it.value }.firstOrNull()
        if(syntax == null) {
            syntax = syntaxes.get(emptyList())
            if(syntax == null) {
                logger.error("Please provide an valid executor.")
                return
            }
            syntax.accept(arguments)
            return
        }

        syntax.accept(arguments)
    }

    fun setExecutor(runnable: Runnable) {
        val context: Consumer<Map<ArgumentType, Any>> = Consumer { _ -> runnable.run() }
        addSyntax(context, emptyList())
    }

    fun addSyntax(context: Consumer<Map<ArgumentType, Any>>, arguments: List<ArgumentType>) {
        syntaxes[arguments] = context
    }
}