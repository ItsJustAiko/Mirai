package me.aikoo.mirai.core

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class EventListener(private val jda: MiraiClient) : ListenerAdapter() {
    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        jda.getCommandByName(event.name)?.execute(jda, event)
    }
}