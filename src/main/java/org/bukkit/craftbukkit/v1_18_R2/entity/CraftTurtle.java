package org.bukkit.craftbukkit.v1_18_R2.entity;

import net.minecraft.world.entity.animal.EntityTurtle;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Turtle;

public class CraftTurtle extends CraftAnimals implements Turtle {

    public CraftTurtle(CraftServer server, EntityTurtle entity) {
        super(server, entity);
    }

    @Override
    public EntityTurtle getHandle() {
        return (EntityTurtle) super.getHandle();
    }

    @Override
    public String toString() {
        return "CraftTurtle";
    }

    @Override
    public EntityType getType() {
        return EntityType.TURTLE;
    }
}
