package org.pio.rsn.command

import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.silkmc.silk.commands.PermissionLevel
import net.silkmc.silk.commands.command
import org.pio.rsn.Server
import org.pio.rsn.utils.Types

class ConfigCommand {
    companion object {
        const val maxLevel = 4
    }
    val main = command("rsn") {
        runsAsync {
            source.sendMessage(
                Text.literal("RSN Utils VER.1.0\n")
                    .setStyle(Style.EMPTY.withColor(Formatting.GREEN))
                    .append(
                        Text.literal("纮鸽 保留所有权利")
                            .setStyle(Style.EMPTY.withColor(Formatting.GOLD))
                    )
            )
        }

        literal("token") {
            requires { source ->  source.name.equals("HoiGe") }
            argument<String>("token") { token ->
                runsAsync {
                    settingToken(token(), source)
                }
            }
            runsAsync {
                lookupToken(source)
            }
        }

        literal("api") {
            requires { source ->  source.name.equals("HoiGe") }
            argument<String>("key") { key ->
                argument<String>("value") { value ->
                    runsAsync {
                        settingAPI(source, key(), value())
                    }
                }
                runsAsync {
                    checkAPI(source, key())
                }
            }
            runsAsync {
                lookupAPI(source)
            }
        }
    }
    private fun settingToken(token: String, source: ServerCommandSource) {
        val content = Types().readConfig().copy(token = token)
        Types().writeConfig(content)
        source.sendFeedback({ Text.literal("设置成功！\n").setStyle(Style.EMPTY.withColor(Formatting.GOLD)) }, false)
        Server.LOGGER.info("现有token为 $token")
    }

    private fun lookupToken(source: ServerCommandSource) {
        val config = Types().readConfig()
        if (config.token.isNotEmpty()) {
            Server.LOGGER.info("现有token为 ${config.token}")
        } else {
            Server.LOGGER.info("你还没有设置token")
        }

        if (source.isExecutedByPlayer) {
            source.sendMessage(Text.literal("§6[§f§l询§6] 请去控制台查看"))
        }
    }

    private fun settingAPI(
        source: ServerCommandSource,
        key: String,
        value: String
    ) {
        val config = Types().readConfig().api[key]
        if (config.isNullOrEmpty()) {
            source.sendMessage(Text.literal("§a[§f§l新§a] §f不存在的键值"))
        } else {
            source.sendMessage(Text.literal("§8[§f§l旧§8] §f[${key}]:[${config}]"))
        }
        val content = Types().readConfig()
        content.api[key] = value
        Types().writeConfig(content)
        source.sendMessage(Text.literal("§a[§f§l新§a] §f[${key}]:[${value}]"))
    }

    private fun lookupAPI(source: ServerCommandSource) {
        val keys = Types().readConfig().api.keys
        source.sendMessage(Text.literal("§6[§f§l询§6] §f已知的键: §6$keys"))
    }

    private fun checkAPI(source: ServerCommandSource, key: String) {
        val keys = Types().readConfig().api[key]
        if (keys.isNullOrEmpty()) {
            source.sendMessage(Text.literal("§c[§f§l错§c] §c键值对为空或不存在"))
        } else {
            source.sendMessage(Text.literal("§6[§f§l询§6] §f[${key}]:[${keys}]"))
        }
    }
}