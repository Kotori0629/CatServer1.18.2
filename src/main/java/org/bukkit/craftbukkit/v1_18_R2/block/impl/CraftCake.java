/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_18_R2.block.impl;

import org.bukkit.craftbukkit.v1_18_R2.block.data.CraftBlockData;

public final class CraftCake extends CraftBlockData implements org.bukkit.block.data.type.Cake {

    public CraftCake() {
        super();
    }

    public CraftCake(net.minecraft.world.level.block.state.IBlockData state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.type.CraftCake

    private static final net.minecraft.world.level.block.state.properties.BlockStateInteger BITES = getInteger(net.minecraft.world.level.block.BlockCake.class, "bites");

    @Override
    public int getBites() {
        return get(BITES);
    }

    @Override
    public void setBites(int bites) {
        set(BITES, bites);
    }

    @Override
    public int getMaximumBites() {
        return getMax(BITES);
    }
}
