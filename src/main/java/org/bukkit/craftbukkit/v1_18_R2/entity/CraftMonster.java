package org.bukkit.craftbukkit.v1_18_R2.entity;

import net.minecraft.world.entity.monster.EntityMonster;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.entity.Monster;

public class CraftMonster extends CraftCreature implements Monster {

    public CraftMonster(CraftServer server, EntityMonster entity) {
        super(server, entity);
    }

    @Override
    public EntityMonster getHandle() {
        return (EntityMonster) entity;
    }

    @Override
    public String toString() {
        return "CraftMonster";
    }
}
