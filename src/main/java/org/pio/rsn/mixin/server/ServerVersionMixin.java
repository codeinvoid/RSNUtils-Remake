package org.pio.rsn.mixin.server;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(net.minecraft.MinecraftVersion.class)
public abstract class ServerVersionMixin {
    /**
     * @author HoiGe
     * @reason CHANGE NAME
     */
    @Overwrite
    public String getName() {
        return "RSNCORE 1.20";
    }
}
