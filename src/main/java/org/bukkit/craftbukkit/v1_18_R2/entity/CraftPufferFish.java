package org.bukkit.craftbukkit.v1_18_R2.entity;

import net.minecraft.world.entity.animal.EntityPufferFish;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PufferFish;

public class CraftPufferFish extends CraftFish implements PufferFish {

    public CraftPufferFish(CraftServer server, EntityPufferFish entity) {
        super(server, entity);
    }

    @Override
    public EntityPufferFish getHandle() {
        return (EntityPufferFish) super.getHandle();
    }

    @Override
    public int getPuffState() {
        return getHandle().getPuffState();
    }

    @Override
    public void setPuffState(int state) {
        getHandle().setPuffState(state);
    }

    @Override
    public String toString() {
        return "CraftPufferFish";
    }

    @Override
    public EntityType getType() {
        return EntityType.PUFFERFISH;
    }
}
