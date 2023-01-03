package me.aikoo.mirai.commands

import com.sun.management.OperatingSystemMXBean
import me.aikoo.mirai.Constants
import me.aikoo.mirai.core.Command
import me.aikoo.mirai.core.MiraiClient
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDAInfo
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import java.awt.Color
import java.lang.management.ManagementFactory
import java.lang.management.MemoryMXBean

class BotInfoCommand : Command(
    name = "info",
    description = "À propos de Mirai!",
    options = listOf()
) {
    override fun execute(client: MiraiClient, event: SlashCommandInteractionEvent) {
        val jdaVersion = JDAInfo.VERSION
        val kotlinVersion = KotlinVersion.CURRENT
        val javaVersion = System.getProperty("java.version")
        val miraiVersion = Constants.VERSION

        val cpuCore = Runtime.getRuntime().availableProcessors()
        val cpuArch = System.getProperty("os.arch")
        val os = System.getProperty("os.name")
        val memorySystemMXBean = ManagementFactory.getMemoryMXBean() as MemoryMXBean
        val ramMax = (ManagementFactory.getOperatingSystemMXBean() as OperatingSystemMXBean).totalMemorySize / 1024 / 1024
        val ramUsage = memorySystemMXBean.heapMemoryUsage.used / 1024 / 1024

        EmbedBuilder()
            .setAuthor("${client.getJDA().selfUser.name} - À propos", null, client.getJDA().selfUser.avatarUrl)
            .setThumbnail(client.getJDA().selfUser.avatarUrl)
            .setDescription("Développeur : **Aikoo#0001**")
            .addField("${Constants.EMOTE_JAVA} Versions", "Mirai: `${miraiVersion}`\nJDA: `${jdaVersion}`\nKotlin: `${kotlinVersion}`\nJava: `${javaVersion}`", true)
            .addField("${Constants.EMOTE_LINUX} Système", "CPU: `${cpuCore} coeurs` | `${cpuArch}`\nRAM: `${ramUsage}MB/${ramMax}MB`\nOS: `${os}`", true)
            .addBlankField(true)
            .addField("${Constants.EMOTE_STATS} Statistiques", "Serveurs: `${client.getJDA().guildCache.size()}`\nUtilisateurs: `${client.getJDA().userCache.size()}`", true)
            .addField("${Constants.EMOTE_THANKS} Contributeurs", "- **Flaticon & Freepik**", true)
            .addField("${Constants.EMOTE_LINKS} Liens", "`Soon`", true)
            .setFooter("Commande info par ${event.user.name}", event.user.avatarUrl)
            .setTimestamp(event.timeCreated)
            .setColor(Color.decode(Constants.COLOR_DISCORD_BACKGROUND))
            .build()
            .let { event.replyEmbeds(it).queue() }
    }
}
