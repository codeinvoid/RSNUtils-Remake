package org.pio.rsn.mixin.server;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.DisconnectS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.pio.rsn.message.BannedMessage;
import org.pio.rsn.model.Banned;
import org.pio.rsn.model.Whitelist;
import org.pio.rsn.utils.Types;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@Mixin(ServerLoginNetworkHandler.class)
public abstract class ServerLoginNetworkHandleMixin {
    @Shadow @Final ClientConnection connection;
    @Shadow @Final static Logger LOGGER;
    @Shadow @Final MinecraftServer server;
    @Shadow @Nullable GameProfile profile;
    @Shadow
    public String getConnectionInfo() {
        return null;
    }

    private static final AtomicInteger NEXT_LOGGING_THREAD_ID = new AtomicInteger(0);

    /**
     * @author HoiGe
     * @reason 设置封禁
     */
    @Overwrite
    private void addToServer(ServerPlayerEntity player) {
        Thread thread = new Thread(() ->
                getConfig(player), "Logging thread #"+ NEXT_LOGGING_THREAD_ID.incrementAndGet());
        thread.setUncaughtExceptionHandler((threadx, throwable) ->
                LOGGER.error("Uncaught exception in server thread", throwable));
        if (Runtime.getRuntime().availableProcessors() > 4) {
            thread.setPriority(6);
        }
        thread.start();
    }

    private void getConfig(ServerPlayerEntity player) {
        Text text = Text.literal("错误\n").append(Text.literal("API未配置 请向服主或管理员求助"));

        Map<String, String> value = new Types().readConfig().getApi();
        if (value.get("serverAPI") != null && !value.get("serverAPI").isEmpty()) {
            requestBannedList(player);
        } else {
            doDisconnect(text);
        }
    }

    private void requestBannedList(ServerPlayerEntity player) {
        Text text = Text.literal("错误 ").append(Text.literal("API请求失败!"));

        if (new Types().getBanned(player.getUuidAsString()) != null) {
            addPlayerConnect(player);
        } else {
            doDisconnect(text);
        }
    }

    private void addPlayerConnect(ServerPlayerEntity player) {
        Banned banned = new Types().getBanned(player.getUuidAsString());
        Whitelist isWhitelistActive = new Types().getWhitelist(player.getUuidAsString());
        if (banned != null) {
            if (!banned.getActive()) {
                this.server.getPlayerManager().onPlayerConnect(this.connection, player);
                assert isWhitelistActive != null;
                if (!isWhitelistActive.getActive()) {
                    doDisconnect(Text.literal("你未被该服务器列入白名单"));
                }
            } else {
                Text text = new BannedMessage().bannedMessage(banned);
                doDisconnect(text);
            }
        } else {
            Text text = Text.literal("获取玩家信息失败");
            doDisconnect(text);
        }
    }

    /**
     * @author HoiGe
     * @reason 修改断开连接通知 眼不见心不烦
     */
    @Overwrite
    public void onDisconnected(Text reason) {
        Thread thread = new Thread(() ->
                sendDisconnectedMessages(reason), "Logging thread #"+ NEXT_LOGGING_THREAD_ID.incrementAndGet());
        thread.setUncaughtExceptionHandler((threadx, throwable) ->
                LOGGER.error("Uncaught exception in server thread", throwable));
        if (Runtime.getRuntime().availableProcessors() > 4) {
            thread.setPriority(6);
        }
        thread.start();
    }

    private void sendDisconnectedMessages(Text reason) {
        try {
            assert this.profile != null;
            Banned banned = Objects.requireNonNull(new Types().getBanned(this.profile.getId().toString()));
            if (!banned.getActive()) {
                LOGGER.info("{} lost connection: {}", this.getConnectionInfo(), reason.getString());
            }
        } catch (Exception exception) {
            LOGGER.error("Couldn't get player data.");
            MutableText text2 = Text.translatable("multiplayer.disconnect.invalid_player_data");
            doDisconnect(text2);
        }
    }

    private void doDisconnect(Text text) {
        this.connection.send(new DisconnectS2CPacket(text));
        this.connection.disconnect(text);
    }
}