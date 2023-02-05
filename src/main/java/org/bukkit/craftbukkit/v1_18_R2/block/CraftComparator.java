package org.bukkit.craftbukkit.v1_18_R2.block;

import net.minecraft.world.level.block.entity.TileEntityComparator;
import org.bukkit.World;
import org.bukkit.block.Comparator;

public class CraftComparator extends CraftBlockEntityState<TileEntityComparator> implements Comparator {

    public CraftComparator(World world, TileEntityComparator tileEntity) {
        super(world, tileEntity);
    }
}
