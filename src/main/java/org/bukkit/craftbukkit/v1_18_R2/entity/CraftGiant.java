package org.bukkit.craftbukkit.v1_18_R2.entity;

import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Giant;

public class CraftGiant extends CraftMonster implements Giant {

    public CraftGiant(CraftServer server, Giant entity) {
        super(server, entity);
    }

    @Override
    public Giant getHandle() {
        return (Giant) entity;
    }

    @Override
    public String toString() {
        return "CraftGiant";
    }

    @Override
    public EntityType getType() {
        return EntityType.GIANT;
    }
}
