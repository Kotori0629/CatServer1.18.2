package org.bukkit.craftbukkit.v1_18_R2.block;

import net.minecraft.world.level.block.BlockDispenser;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.TileEntityDispenser;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Dispenser;
import org.bukkit.craftbukkit.v1_18_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftInventory;
import org.bukkit.craftbukkit.v1_18_R2.projectiles.CraftBlockProjectileSource;
import org.bukkit.inventory.Inventory;
import org.bukkit.projectiles.BlockProjectileSource;

public class CraftDispenser extends CraftLootable<TileEntityDispenser> implements Dispenser {

    public CraftDispenser(World world, TileEntityDispenser tileEntity) {
        super(world, tileEntity);
    }

    @Override
    public Inventory getSnapshotInventory() {
        return new CraftInventory(this.getSnapshot());
    }

    @Override
    public Inventory getInventory() {
        if (!this.isPlaced()) {
            return this.getSnapshotInventory();
        }

        return new CraftInventory(this.getTileEntity());
    }

    @Override
    public BlockProjectileSource getBlockProjectileSource() {
        Block block = getBlock();

        if (block.getType() != Material.DISPENSER) {
            return null;
        }

        return new CraftBlockProjectileSource((TileEntityDispenser) this.getTileEntityFromWorld());
    }

    @Override
    public boolean dispense() {
        ensureNoWorldGeneration();
        Block block = getBlock();
        if (block.getType() == Material.DISPENSER) {
            CraftWorld world = (CraftWorld) this.getWorld();
            BlockDispenser dispense = (BlockDispenser) Blocks.DISPENSER;

            dispense.dispenseFrom(world.getHandle(), this.getPosition());
            return true;
        } else {
            return false;
        }
    }
}
