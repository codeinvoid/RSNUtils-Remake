package org.pio.rsn

import net.fabricmc.api.ModInitializer
import net.minecraft.MinecraftVersion
import org.pio.rsn.command.BannedCommand
import org.pio.rsn.command.ConfigCommand
import org.pio.rsn.command.WhitelistCommand
import org.pio.rsn.config.Config
import org.pio.rsn.config.ConfigOperator
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File


@Suppress("UNUSED")
object Server: ModInitializer {
	val LOGGER: Logger = LoggerFactory.getLogger("RSNUtils")
	fun configFile() = File("./config/RSNUtils.toml")

	override fun onInitialize() {
		LOGGER.info("""
                     
                     ___           ___           ___    \s
                    /\\  \\         /\\  \\         /\\__\\   \s
                   /::\\  \\       /::\\  \\       /::|  |  \s
                  /:/\\:\\  \\     /:/\\ \\  \\     /:|:|  |  \s
                 /::\\~\\:\\  \\   _\\:\\~\\ \\  \\   /:/|:|  |__\s
                /:/\\:\\ \\:\\__\\ /\\ \\:\\ \\ \\__\\ /:/ |:| /\\__\\
                \\/_|::\\/:/  / \\:\\ \\:\\ \\/__/ \\/__|:|/:/  /
                   |:|::/  /   \\:\\ \\:\\__\\       |:/:/  /\s
                   |:|\\/__/     \\:\\/:/  /       |::/  / \s
                   |:|  |        \\::/  /        /:/  /  \s
                    \\|__|         \\/__/         \\/__/   \s""");
		LOGGER.info(MinecraftVersion.CURRENT.name);
		if (!configFile().exists()) {
			configFile().createNewFile()
			val content = Config("", mutableMapOf("serverAPI" to ""))
			ConfigOperator(configFile()).write(Config::class.java, content)
		}
		LOGGER.info("Config is loaded!")
		WhitelistCommand()
		BannedCommand()
		ConfigCommand()
		LOGGER.info("Commands is loaded!")
	}
}