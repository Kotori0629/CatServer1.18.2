package org.bukkit.craftbukkit.v1_18_R2.entity;

import net.minecraft.world.entity.projectile.EntityEgg;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.entity.Egg;
import org.bukkit.entity.EntityType;

public class CraftEgg extends CraftThrowableProjectile implements Egg {
    public CraftEgg(CraftServer server, EntityEgg entity) {
        super(server, entity);
    }

    @Override
    public EntityEgg getHandle() {
        return (EntityEgg) entity;
    }

    @Override
    public String toString() {
        return "CraftEgg";
    }

    @Override
    public EntityType getType() {
        return EntityType.EGG;
    }
}