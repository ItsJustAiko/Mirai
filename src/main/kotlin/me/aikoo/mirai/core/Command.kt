package me.aikoo.mirai.core

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.Commands
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData

abstract class Command(
    private var name: String,
    private var description: String,
    private var options: List<OptionData>
) {
    fun getName(): String {
        return name
    }

    fun getDescription(): String {
        return description
    }

    fun getOptions(): List<OptionData> {
        return options
    }

    abstract fun execute(client: MiraiClient, event: SlashCommandInteractionEvent)

    fun buildData(): SlashCommandData {
        val data = Commands.slash(name, description)
        data.addOptions(options)
        return data
    }
}