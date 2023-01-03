package me.aikoo.mirai

import com.google.gson.JsonParser
import java.net.HttpURLConnection
import java.net.URL

class Utils {
    companion object {
        fun getHugURL(): String? {
            return getURL("hug")
        }

        fun getKissURL(): String? {
            return getURL("kiss")
        }

        private fun getURL(name: String): String? {
            val url = URL("https://api.otakugifs.xyz/gif?reaction=${name}")

            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "GET"
            val inputStream = connection.inputStream
            val json = inputStream.bufferedReader().use { it.readText() }
            val gifUrl = JsonParser.parseString(json).asJsonObject["url"].asString

            return if (connection.responseCode == 200) gifUrl else null
        }
    }
}