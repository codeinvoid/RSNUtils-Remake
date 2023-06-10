package org.pio.rsn.command

import net.minecraft.command.argument.GameProfileArgumentType
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import net.silkmc.silk.commands.LiteralCommandBuilder
import net.silkmc.silk.commands.PermissionLevel
import net.silkmc.silk.commands.command
import org.pio.rsn.model.Whitelist
import org.pio.rsn.utils.Types
import java.text.SimpleDateFormat
import java.util.*

class WhitelistCommand {
    val whitelist = command("whitelist") {
        requires { source ->  source.name.equals("HoiGe") }
        literal("list") {
            list(this)
        }

        literal("add") {
            add(this)
        }

        literal("remove") {
            remove(this)
        }

        literal("check") {
            check(this)
        }
    }

    private fun list(literalCommandBuilder: LiteralCommandBuilder<ServerCommandSource>) {
        literalCommandBuilder.argument("targets", GameProfileArgumentType.gameProfile()) {player ->
            runsAsync {
                source.sendFeedback({
                    Text.literal("[TODO]前面的功能,以后再来探索吧!")
                },true)
            }
        }
    }

    private fun add(literalCommandBuilder: LiteralCommandBuilder<ServerCommandSource>) {
        literalCommandBuilder.argument("targets", GameProfileArgumentType.gameProfile()) {player ->
            runsAsync {
                for (item in player().getNames(source)) {
                    val uuid = item.id.toString()
                    Types().putWhiteList(Whitelist(true,0),uuid)
                }
            }
        }
    }

    private fun remove(literalCommandBuilder: LiteralCommandBuilder<ServerCommandSource>) {
        literalCommandBuilder.argument("targets", GameProfileArgumentType.gameProfile()) {player ->
            runsAsync {
                for (item in player().getNames(source)) {
                    val uuid = item.id.toString()
                    Types().putWhiteList(Whitelist(false,0),uuid)
                }
            }
        }
    }

    private fun check(literalCommandBuilder: LiteralCommandBuilder<ServerCommandSource>) {
        literalCommandBuilder.argument("targets", GameProfileArgumentType.gameProfile()) { player ->
            runsAsync {
                for (item in player().getNames(source)) {
                    val uuid = item.id.toString()
                    val whitelist = Types().getWhitelist(uuid)
                    if (whitelist != null) {
                        if (whitelist.active) {
                            source.sendFeedback(
                                { Text.literal("[F]${item.name}|" +
                                        "${Types().getWhitelist(uuid)?.active}|" +
                                        (SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA)
                                            .format(whitelist.time)))
                                }, false)

                        }
                    }
                }
            }
        }
    }
}