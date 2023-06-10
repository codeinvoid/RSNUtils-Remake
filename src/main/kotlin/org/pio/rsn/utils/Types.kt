package org.pio.rsn.utils

import org.pio.rsn.Server
import org.pio.rsn.config.Config
import org.pio.rsn.config.ConfigOperator
import org.pio.rsn.model.Banned
import org.pio.rsn.model.Whitelist

class Types {
    /**
     * Config
     */
    fun readConfig() = ConfigOperator(Server.configFile()).read(Config::class.java)
    fun writeConfig(content: Config) = ConfigOperator(Server.configFile()).write(Config::class.java, content)

    /**
     * API
     */
    private val playerAPI = readConfig().api["serverAPI"].toString()

    /**
     * GET
     */
    fun getBanned(uuid : String) = Request()
        .request(Banned::class.java, "banned", uuid, playerAPI)

    fun getWhitelist(uuid : String) = Request()
        .request(Whitelist::class.java, "whitelist", uuid, playerAPI)

    /**
     * PUT
     */
    fun putBanned(banned: Banned, uuid: String) = Request()
        .put(banned, "banned", uuid, playerAPI)

    fun putWhiteList(whitelist: Whitelist, uuid: String) = Request()
        .put(whitelist, "whitelist", uuid, playerAPI)

}