package org.bukkit.craftbukkit.v1_18_R2.tag;

import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.core.IRegistry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.FluidType;
import org.bukkit.Fluid;
import org.bukkit.craftbukkit.v1_18_R2.util.CraftMagicNumbers;

public class CraftFluidTag extends CraftTag<FluidType, Fluid> {

    public CraftFluidTag(IRegistry<FluidType> registry, TagKey<FluidType> tag) {
        super(registry, tag);
    }

    @Override
    public boolean isTagged(Fluid fluid) {
        return CraftMagicNumbers.getFluid(fluid).is(tag);
    }

    @Override
    public Set<Fluid> getValues() {
        return getHandle().stream().map((fluid) -> CraftMagicNumbers.getFluid(fluid.value())).collect(Collectors.toUnmodifiableSet());
    }
}
