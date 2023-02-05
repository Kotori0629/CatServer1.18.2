package org.bukkit.craftbukkit.v1_18_R2.entity;

import net.minecraft.world.entity.monster.piglin.EntityPiglinBrute;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PiglinBrute;

public class CraftPiglinBrute extends CraftPiglinAbstract implements PiglinBrute {

    public CraftPiglinBrute(CraftServer server, EntityPiglinBrute entity) {
        super(server, entity);
    }

    @Override
    public EntityPiglinBrute getHandle() {
        return (EntityPiglinBrute) super.getHandle();
    }

    @Override
    public EntityType getType() {
        return EntityType.PIGLIN_BRUTE;
    }

    @Override
    public String toString() {
        return "CraftPiglinBrute";
    }
}
