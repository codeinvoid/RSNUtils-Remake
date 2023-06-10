package org.pio.rsn.mixin.server;

import net.minecraft.obfuscate.DontObfuscate;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerVersionMixin {
    /**
     * @author HoiGe
     * @reason CHANGE MOD NAME
     */
    @Overwrite(remap = false)
    public String getServerModName() {
        return "RSNCORE";
    }
}
