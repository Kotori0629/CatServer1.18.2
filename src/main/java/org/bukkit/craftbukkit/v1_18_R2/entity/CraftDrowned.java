package org.bukkit.craftbukkit.v1_18_R2.entity;

import net.minecraft.world.entity.monster.EntityDrowned;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.entity.Drowned;
import org.bukkit.entity.EntityType;

public class CraftDrowned extends CraftZombie implements Drowned {

    public CraftDrowned(CraftServer server, EntityDrowned entity) {
        super(server, entity);
    }

    @Override
    public EntityDrowned getHandle() {
        return (EntityDrowned) entity;
    }

    @Override
    public String toString() {
        return "CraftDrowned";
    }

    @Override
    public EntityType getType() {
        return EntityType.DROWNED;
    }
}
