/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_18_R2.block.impl;

import org.bukkit.craftbukkit.v1_18_R2.block.data.CraftBlockData;

public final class CraftTallPlant extends CraftBlockData implements org.bukkit.block.data.Bisected {

    public CraftTallPlant() {
        super();
    }

    public CraftTallPlant(net.minecraft.world.level.block.state.IBlockData state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftBisected

    private static final net.minecraft.world.level.block.state.properties.BlockStateEnum<?> HALF = getEnum(net.minecraft.world.level.block.BlockTallPlant.class, "half");

    @Override
    public org.bukkit.block.data.Bisected.Half getHalf() {
        return get(HALF, org.bukkit.block.data.Bisected.Half.class);
    }

    @Override
    public void setHalf(org.bukkit.block.data.Bisected.Half half) {
        set(HALF, half);
    }
}
