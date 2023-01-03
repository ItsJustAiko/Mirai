package me.aikoo.mirai.commands

import me.aikoo.mirai.Constants
import me.aikoo.mirai.core.Command
import me.aikoo.mirai.core.MiraiClient
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import java.awt.Color

class PingCommand : Command(
    name = "ping",
    description = "La latence de Mirai!",
    options = listOf()
) {
    override fun execute(client: MiraiClient, event: SlashCommandInteractionEvent) {
        val ping = client.getJDA().gatewayPing
        event.channel.sendMessage("${Constants.LOADING} **Chargement...**").queue { message ->
            val botPing = System.currentTimeMillis() - message.timeCreated.toInstant().toEpochMilli()

            val embed = EmbedBuilder()
                .setTitle("${Constants.EMOTE_PING} Pong! Latence de Mirai")
                .setThumbnail(client.getJDA().selfUser.avatarUrl)
                .addField("${Constants.EMOTE_MIRAI} Mirai", "`${botPing}ms` | `${getQuality(botPing)}`", false )
                .addField("${Constants.EMOTE_DISCORD} API Discord", "`${ping}ms` | `${getQuality(ping)}`", false)
                .setFooter("Commande ping par ${event.user.name}", event.user.avatarUrl)
                .setTimestamp(event.timeCreated)
                .setColor(Color.decode(Constants.COLOR_DISCORD_BACKGROUND))
                .build()
            event.replyEmbeds(embed).queue()
            message.delete().queue()
        }
    }

    private fun getQuality(num: Long): String {
        return when {
            num < 100 -> "Excellente"
            num < 200 -> "Bonne"
            num < 300 -> "Correcte"
            num < 400 -> "Mauvaise"
            else -> "Terrible"
        }
    }
}