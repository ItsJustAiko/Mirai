package me.aikoo.mirai

import me.aikoo.mirai.core.MiraiClient

fun main(args: Array<String>) {
    System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "info")
    if (args[0].isEmpty() || args[1].isEmpty()) {
        println("Veuillez entrer un token et un mode de production!")
        return
    }

    MiraiClient(args[0], args[1]).build()
}