package org.bukkit.craftbukkit.v1_18_R2.entity;

import net.minecraft.world.entity.projectile.EntityEnderPearl;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.EntityType;

public class CraftEnderPearl extends CraftThrowableProjectile implements EnderPearl {
    public CraftEnderPearl(CraftServer server, EntityEnderPearl entity) {
        super(server, entity);
    }

    @Override
    public EntityEnderPearl getHandle() {
        return (EntityEnderPearl) entity;
    }

    @Override
    public String toString() {
        return "CraftEnderPearl";
    }

    @Override
    public EntityType getType() {
        return EntityType.ENDER_PEARL;
    }
}
