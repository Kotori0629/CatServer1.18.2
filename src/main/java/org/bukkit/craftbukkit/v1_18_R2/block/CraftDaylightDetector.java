package org.bukkit.craftbukkit.v1_18_R2.block;

import net.minecraft.world.level.block.entity.TileEntityLightDetector;
import org.bukkit.World;
import org.bukkit.block.DaylightDetector;

public class CraftDaylightDetector extends CraftBlockEntityState<TileEntityLightDetector> implements DaylightDetector {

    public CraftDaylightDetector(World world, TileEntityLightDetector tileEntity) {
        super(world, tileEntity);
    }
}
