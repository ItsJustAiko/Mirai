package me.aikoo.mirai.core

import org.reflections.Reflections
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class CommandManager(private val client: MiraiClient) {
    private val commands: MutableMap<String, Command> = mutableMapOf()
    private val LOGGER: Logger? = LoggerFactory.getLogger(CommandManager::class.java)

    fun loadCommands() {
        LOGGER?.info("Starting command loading...")
        val reflections = Reflections("me.aikoo.mirai.commands")
        val commandClasses = reflections.getSubTypesOf(Command::class.java)

        for (commandClass in commandClasses) {
            val command = commandClass.getConstructor().newInstance()
            commands[command.getName()] = command
            LOGGER?.info("Loaded command ${command.getName()}")
        }

        LOGGER?.info("Finished command loading!")
    }

    fun getCommands(): Map<String, Command> {
        return commands
    }
}