/**
 * Automatically generated file, changes will be lost.
 */
package org.bukkit.craftbukkit.v1_18_R2.block.impl;

import org.bukkit.craftbukkit.v1_18_R2.block.data.CraftBlockData;

public final class CraftGrindstone extends CraftBlockData implements org.bukkit.block.data.type.Grindstone, org.bukkit.block.data.Directional, org.bukkit.block.data.FaceAttachable {

    public CraftGrindstone() {
        super();
    }

    public CraftGrindstone(net.minecraft.world.level.block.state.IBlockData state) {
        super(state);
    }

    // org.bukkit.craftbukkit.block.data.CraftDirectional

    private static final net.minecraft.world.level.block.state.properties.BlockStateEnum<?> FACING = getEnum(net.minecraft.world.level.block.BlockGrindstone.class, "facing");

    @Override
    public org.bukkit.block.BlockFace getFacing() {
        return get(FACING, org.bukkit.block.BlockFace.class);
    }

    @Override
    public void setFacing(org.bukkit.block.BlockFace facing) {
        set(FACING, facing);
    }

    @Override
    public java.util.Set<org.bukkit.block.BlockFace> getFaces() {
        return getValues(FACING, org.bukkit.block.BlockFace.class);
    }

    // org.bukkit.craftbukkit.block.data.CraftFaceAttachable

    private static final net.minecraft.world.level.block.state.properties.BlockStateEnum<?> ATTACH_FACE = getEnum(net.minecraft.world.level.block.BlockGrindstone.class, "face");

    @Override
    public org.bukkit.block.data.FaceAttachable.AttachedFace getAttachedFace() {
        return get(ATTACH_FACE, org.bukkit.block.data.FaceAttachable.AttachedFace.class);
    }

    @Override
    public void setAttachedFace(org.bukkit.block.data.FaceAttachable.AttachedFace face) {
        set(ATTACH_FACE, face);
    }
}