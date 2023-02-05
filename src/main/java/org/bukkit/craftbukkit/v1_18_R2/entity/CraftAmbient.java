package org.bukkit.craftbukkit.v1_18_R2.entity;

import net.minecraft.world.entity.ambient.EntityAmbient;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.entity.Ambient;
import org.bukkit.entity.EntityType;

public class CraftAmbient extends CraftMob implements Ambient {
    public CraftAmbient(CraftServer server, EntityAmbient entity) {
        super(server, entity);
    }

    @Override
    public EntityAmbient getHandle() {
        return (EntityAmbient) entity;
    }

    @Override
    public String toString() {
        return "CraftAmbient";
    }

    @Override
    public EntityType getType() {
        return EntityType.UNKNOWN;
    }
}
