package org.pio.rsn.message

import net.minecraft.text.MutableText
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import org.pio.rsn.model.Banned
import java.text.SimpleDateFormat
import java.util.*

class BannedMessage {
    fun bannedMessage(banned: Banned) : MutableText {
        return Text
            .literal("你已被封禁\n\n")
            .setStyle(Style.EMPTY.withColor(Formatting.RED).withBold(true))
            .append(
                Text.literal("原因 ❯❯ ")
                    .setStyle(Style.EMPTY.withColor(Formatting.WHITE).withBold(false)))
            .append(
                Text.literal(bannedReason(banned))
                    .setStyle(Style.EMPTY.withColor(Formatting.RED).withBold(false)))
            .append(
                Text.literal("\n封禁编号 ❯❯ ")
                    .setStyle(Style.EMPTY.withColor(Formatting.WHITE).withBold(false)))
            .append(
                Text.literal(banned.nanoid+"\n")
                    .setStyle(Style.EMPTY.withColor(Formatting.RED).withBold(false)))
            .append(
                Text.literal("操作时间 ❯❯ ")
                    .setStyle(Style.EMPTY.withColor(Formatting.WHITE).withBold(false)))
            .append(
                Text.literal(SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(banned.time)+ "\n")
                    .setStyle(Style.EMPTY.withColor(Formatting.RED).withBold(false)))
            .append(
                Text.literal("申诉 ❯❯ ")
                    .setStyle(Style.EMPTY.withColor(Formatting.WHITE).withBold(false)))
            .append(Text.literal("(邮箱) issue@p-io.org").setStyle(Style.EMPTY.withBold(false)))
    }

    private fun bannedReason(banned: Banned): String {
        return if (banned.reason == "null" || banned.reason == "") {
            "无"
        } else {
            banned.reason
        }
    }
}