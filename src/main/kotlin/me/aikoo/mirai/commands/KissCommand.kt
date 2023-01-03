package me.aikoo.mirai.commands

import com.google.gson.JsonParser
import me.aikoo.mirai.Constants
import me.aikoo.mirai.Utils
import me.aikoo.mirai.core.Command
import me.aikoo.mirai.core.MiraiClient
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.User
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.OptionData
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.buttons.Button
import java.net.HttpURLConnection
import java.net.URL
import java.time.temporal.TemporalAccessor

class KissCommand : Command(
    name = "kiss",
    description = "Faire un bisous ! <3",
    options = listOf(
        OptionData(OptionType.USER, "user", "La personne à embrasser", true)
    )
) {
    override fun execute(client: MiraiClient, event: SlashCommandInteractionEvent) {
        val user = event.getOption("user")!!.asUser
        val gifUrl = Utils.getKissURL()

        if (gifUrl == null) {
            event.reply("${Constants.ERROR} **- Une erreur est survenue lors de la récupération de votre image !**").setEphemeral(true).queue()
            return
        }

        event.replyEmbeds(getEmbed(client, event.user, event.timeCreated, gifUrl, user, false))
            .addActionRow(Button.primary("kiss_cmd", "Faire un gros bisous"))
            .queue()

        client.getJDA().addEventListener(KissListener(client, user.id))
    }

    class KissListener(private val client: MiraiClient, private val userId: String) : ListenerAdapter() {
        override fun onButtonInteraction(event: ButtonInteractionEvent) {
            if (event.componentId == "kiss_cmd" && !event.isAcknowledged) {
                if (event.user.id != userId) {
                    println(event.user.id + " " + userId)
                    event.reply("${Constants.ERROR} **- Vous n'êtes pas la personne à qui on a fait un bisous ! >:(**").setEphemeral(true).queue()
                    return
                }

                val gifUrl = Utils.getKissURL()
                if (gifUrl == null) {
                    event.reply("${Constants.ERROR} **- Une erreur est survenue lors de la récupération de votre image !**").setEphemeral(true).queue()
                    return
                }

                event.message.editMessageComponents().setActionRows(ActionRow.of(Button.primary("kiss_cmd", "Faire un gros bisous").asDisabled())).queue()
                event.replyEmbeds(KissCommand().getEmbed(client, event.user, event.timeCreated, gifUrl, event.user, true)).queue()
            }
        }
    }

    private fun getEmbed(client: MiraiClient, author: User, creationTime: TemporalAccessor, gifUrl: String, user: User, isBack: Boolean): MessageEmbed {
        val randomColor = (0..0xFFFFFF).random()
        val sentencesArray = listOf("Trop mignon ! <3", "Ouuuh les amoureux !", "Fait moi un bisous aussi !", "Sur la joue c'est moins sale d'abord >:(", "Mooh")
        val sentence = if (user.id == client.getJDA().selfUser.id)  "Merci pour le bisous <3" else sentencesArray.random()

        val title = if (isBack) "${author.name} fait un bisous en retour à ${user.name}!" else "${author.name} fait un bisous à ${user.name}!"

        return EmbedBuilder()
            .setTitle(title)
            .setDescription(sentence)
            .setImage(gifUrl)
            .setColor(randomColor)
            .setFooter("Commande kiss par ${author.name}", author.avatarUrl)
            .setTimestamp(creationTime)
            .build()
    }
}
