package org.bukkit.craftbukkit.v1_18_R2.entity;

import com.google.common.base.Preconditions;
import net.minecraft.world.entity.animal.EntityParrot;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Parrot;

public class CraftParrot extends CraftTameableAnimal implements Parrot {

    public CraftParrot(CraftServer server, EntityParrot parrot) {
        super(server, parrot);
    }

    @Override
    public EntityParrot getHandle() {
        return (EntityParrot) entity;
    }

    @Override
    public Variant getVariant() {
        return Variant.values()[getHandle().getVariant()];
    }

    @Override
    public void setVariant(Variant variant) {
        Preconditions.checkArgument(variant != null, "variant");

        getHandle().setVariant(variant.ordinal());
    }

    @Override
    public String toString() {
        return "CraftParrot";
    }

    @Override
    public EntityType getType() {
        return EntityType.PARROT;
    }
}
