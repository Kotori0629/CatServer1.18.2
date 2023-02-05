package org.bukkit.craftbukkit.v1_18_R2;

import net.minecraft.world.level.RayTrace.FluidCollisionOption;
import org.bukkit.FluidCollisionMode;

public final class CraftFluidCollisionMode {

    private CraftFluidCollisionMode() {}

    public static FluidCollisionOption toNMS(FluidCollisionMode fluidCollisionMode) {
        if (fluidCollisionMode == null) return null;

        switch (fluidCollisionMode) {
            case ALWAYS:
                return FluidCollisionOption.ANY;
            case SOURCE_ONLY:
                return FluidCollisionOption.SOURCE_ONLY;
            case NEVER:
                return FluidCollisionOption.NONE;
            default:
                return null;
        }
    }
}
