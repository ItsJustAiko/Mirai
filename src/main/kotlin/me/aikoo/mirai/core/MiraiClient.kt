package me.aikoo.mirai.core

import me.aikoo.mirai.Constants
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MiraiClient(private val token: String, private val mode: String) {
    private var jdaBuilder: JDABuilder = JDABuilder.createDefault(token).addEventListeners(EventListener(this))
    private var commandManager: CommandManager = CommandManager(this)
    private lateinit var jda: JDA
    private val LOGGER: Logger? = LoggerFactory.getLogger(MiraiClient::class.java)

    fun build(): JDA {
        jda = jdaBuilder.build()
        jda.presence.activity = Activity.watching("les étoiles")
        commandManager.loadCommands()
        LOGGER?.info("Loaded ${commandManager.getCommands().size} commands!")
        LOGGER?.info("Starting registration of commands...")
        val updateCommand = jda.updateCommands()

        for (command in commandManager.getCommands().values) {
            updateCommand.addCommands(command.buildData())
            LOGGER?.info("Registered command ${command.getName()}")
        }
        LOGGER?.info("Finished registration of commands!")

        updateCommand.queue {
            LOGGER?.info("Successfully updated ${it.size} commands!")
        }
        LOGGER?.info("Finished building!")

        return jda
    }

    fun getJDA(): JDA {
        return jda
    }

    fun getCommandByName(name: String): Command? {
        return commandManager.getCommands()[name]
    }
}