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

class HugCommand : Command(
    name = "hug",
    description = "Faire un grooos câlin !",
    options = listOf(
        OptionData(OptionType.USER, "user", "La personne à câliner", true)
    )
) {
    override fun execute(client: MiraiClient, event: SlashCommandInteractionEvent) {
        val user = event.getOption("user")!!.asUser
        val gifUrl = Utils.getHugURL()

        if (gifUrl == null) {
            event.reply("${Constants.ERROR} **- Une erreur est survenue lors de la récupération de votre image !**").setEphemeral(true).queue()
            return
        }

        event.replyEmbeds(getEmbed(client, event.user, event.timeCreated, gifUrl, user, false))
            .addActionRow(Button.primary("hug_cmd", "Faire un câlin en retour"))
            .queue()

        client.getJDA().addEventListener(HugListener(client, user.id))
    }

    class HugListener(private val client: MiraiClient, private val userId: String) : ListenerAdapter() {
        override fun onButtonInteraction(event: ButtonInteractionEvent) {
            if (event.componentId == "hug_cmd" && !event.isAcknowledged) {
                if (event.user.id != userId) {
                    event.reply("${Constants.ERROR} **- Vous n'êtes pas la personne à qui j'ai donné un câlin ! >:(**").setEphemeral(true).queue()
                    return
                }

                // Get class HugCommand and get the method getHugURL
                val gifUrl = Utils.getHugURL()
                if (gifUrl == null) {
                    event.reply("${Constants.ERROR} **- Une erreur est survenue lors de la récupération de votre image !**").setEphemeral(true).queue()
                    return
                }

                event.message.editMessageComponents().setActionRows(ActionRow.of(Button.primary("hug_cmd", "Faire un câlin en retour")).asDisabled()).queue()
                event.replyEmbeds(HugCommand().getEmbed(client, event.user, event.timeCreated, gifUrl, event.user, true)).queue()
            }
        }
    }

    private fun getEmbed(client: MiraiClient, author: User, creationTime: TemporalAccessor, gifUrl: String, user: User, isBack: Boolean): MessageEmbed {
        val randomColor = (0..0xFFFFFF).random()
        val sentencesArray = listOf("Trop mignon ! <3", "Ouuuh les amoureux !", "Moi aussi je veux un câlin ;-;", "Les câlins de ma maman sont les meilleurs", "Mooh")
        val sentence = if (user.id == client.getJDA().selfUser.id)  "Merci pour le câlin <3" else sentencesArray.random()

        val title = if (isBack) "${author.name} fait un câlin en retour à ${user.name}!" else "${author.name} fait un câlin à ${user.name}!"

        return EmbedBuilder()
            .setTitle(title)
            .setDescription(sentence)
            .setImage(gifUrl)
            .setColor(randomColor)
            .setFooter("Commande hug par ${author.name}", author.avatarUrl)
            .setTimestamp(creationTime)
            .build()
    }
}
