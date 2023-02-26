package catserver.server;

import catserver.server.bukkit.CraftCustomEnchantment;
import catserver.server.bukkit.CraftCustomPotionEffect;
import catserver.server.utils.EnumHelper;
import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import io.izzel.arclight.api.Unsafe;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraftforge.registries.ForgeRegistries;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_18_R2.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_18_R2.potion.CraftPotionUtil;
import org.bukkit.craftbukkit.v1_18_R2.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.v1_18_R2.util.CraftNamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.Contract;

import java.lang.reflect.Modifier;
import java.util.*;

public class BukkitInjector {

    public static BiMap<ResourceKey<LevelStem>, World.Environment> environments = HashBiMap.create(ImmutableMap.<ResourceKey<LevelStem>, World.Environment>builder().put(LevelStem.OVERWORLD, World.Environment.NORMAL).put(LevelStem.NETHER, World.Environment.NETHER).put(LevelStem.END, World.Environment.THE_END).build());
    private static final Map<Block, Material> BLOCK_MATERIAL = Unsafe.getStatic(CraftMagicNumbers.class, "BLOCK_MATERIAL");
    private static final Map<Item, Material> ITEM_MATERIAL = Unsafe.getStatic(CraftMagicNumbers.class, "ITEM_MATERIAL");
    private static final Map<Material, Item> MATERIAL_ITEM = Unsafe.getStatic(CraftMagicNumbers.class, "MATERIAL_ITEM");
    private static final Map<Material, Block> MATERIAL_BLOCK = Unsafe.getStatic(CraftMagicNumbers.class, "MATERIAL_BLOCK");

    public static void registerAll() {
        registerMaterials();
        registerEnchantments();
        registerPotionEffects();
        registerBiomes(); // TODO
        registerEntities(); // TODO
        registerVillagerProfessions(); // TODO
        registerStatistics(); // TODO
        try {
            for (var field : org.bukkit.Registry.class.getFields()) {
                if (Modifier.isStatic(field.getModifiers()) && field.get(null) instanceof org.bukkit.Registry.SimpleRegistry<?> registry) {
                    registry.reload();
                }
            }
        } catch (Throwable ignored) {
        }
    }

    public static void registerEnvironments(Registry<LevelStem> registry) {

    }

    private static void registerStatistics() {

    }

    private static void registerVillagerProfessions() {

    }

    private static void registerEntities() {

    }

    private static void registerBiomes() {

    }

    private static void registerPotionEffects() {
        int i = 0;
        for (var potion : ForgeRegistries.MOB_EFFECTS.getEntries()) {
            var name = standardize(potion.getValue().getRegistryName());
            CraftCustomPotionEffect potionEffect = new CraftCustomPotionEffect(potion.getValue(), name);
            PotionEffectType.registerPotionEffectType(potionEffect);
            i++;
            CatServer.LOGGER.debug("Save-MobEffect: {}", potionEffect.getName());
        }
        org.bukkit.potion.PotionEffectType.stopAcceptingRegistrations();
        CatServer.LOGGER.info("Registered {} mob effect into Bukkit", i);
        // Register potion type
        int ordinal = EntityType.values().length;
        List<PotionType> potionTypes = Lists.newArrayList();
        BiMap<PotionType, String> regularMap = HashBiMap.create(CraftPotionUtil.regular);
        for (var potionType : ForgeRegistries.POTIONS.getEntries()) {
            if (CraftPotionUtil.toBukkit(potionType.getValue().getRegistryName().toString()).getType() == PotionType.UNCRAFTABLE && potionType.getValue() != Potions.EMPTY) {
                var name = standardize(potionType.getValue().getRegistryName());
                MobEffectInstance effectInstance = potionType.getValue().getEffects().isEmpty() ? null : potionType.getValue().getEffects().get(0);
                PotionType type = EnumHelper.makeEnum(PotionType.class, name, ordinal++, Arrays.asList(PotionEffectType.class, boolean.class, boolean.class), Arrays.asList(effectInstance == null ? null : PotionEffectType.getById(MobEffect.getId(effectInstance.getEffect())), false, false));
                potionTypes.add(type);
                regularMap.put(type, potionType.getValue().getRegistryName().toString());
                CatServer.LOGGER.debug("Save-PotionType: {}", type);
            }
        }
        CatServer.LOGGER.info("Registered {} new potion type into Bukkit", regularMap.size());
        EnumHelper.addEnums(PotionType.class, potionTypes);
    }

    private static void registerEnchantments() {
        int i = 0;
        for (var enchantment : ForgeRegistries.ENCHANTMENTS.getEntries()) {
            var name = standardize(enchantment.getValue().getRegistryName());
            CraftCustomEnchantment enchantmentCb = new CraftCustomEnchantment(enchantment.getValue(), name);
            Enchantment.registerEnchantment(enchantmentCb);
            i++;
            CatServer.LOGGER.debug("Save-Enchantment: {}", enchantmentCb.getName());
        }
        org.bukkit.enchantments.Enchantment.stopAcceptingRegistrations();
        CatServer.LOGGER.info("Registered {} enchantments into Bukkit", i);
    }

    private static void registerMaterials() {
        int length = Material.values().length;
        List<Material> values = Lists.newArrayList();
        int blocks = 0, items = 0;
        for (var entry : ForgeRegistries.BLOCKS.getEntries()) {
            var location = entry.getKey().location();
            var blockName = standardize(location);
            var block = entry.getValue();
            var item = ForgeRegistries.ITEMS.getValue(location);
            try {
                Class<?> match = CraftBlockData.getClosestBlockDataClass(block.getClass());
                Class<?> blockClass = match == null ? null : match.getInterfaces()[0];
                Material material;
                if (blockClass == null) {
                    material = Material.addMaterial(blockName, length, CraftNamespacedKey.fromMinecraft(location), true, item != null && item != Items.AIR);
                } else {
                    material = Material.addMaterial(blockName, length, blockClass, CraftNamespacedKey.fromMinecraft(location), true, item != null && item != Items.AIR);
                }
                if (material == null) {
                    CatServer.LOGGER.error("Cannot register new forgeBlock : " + blockName);
                    continue;
                }
                length++;
                values.add(material);
                blocks++;
                BLOCK_MATERIAL.put(block, material);
                MATERIAL_BLOCK.put(material, block);
                CatServer.LOGGER.debug("Save-Block: " + material.name() + " - " + blockName);
            } catch (Throwable e) {
                CatServer.LOGGER.error("Cannot register new forgeBlock : " + blockName);
                e.printStackTrace();
            }
        }
        CatServer.LOGGER.info("Registered {} blocks into Bukkit", blocks);

        for (var entry : ForgeRegistries.ITEMS.getEntries()) {
            var location = entry.getKey().location();
            if (location.getNamespace().equals(NamespacedKey.MINECRAFT)) {
                continue;
            }
            var itemName = standardize(location);
            var item = entry.getValue();
            var material = Material.getMaterial(itemName);
            if (material == null) {
                try {
                    material = Material.addMaterial(itemName, length, CraftNamespacedKey.fromMinecraft(location), false, true);
                    if (material == null) {
                        CatServer.LOGGER.error("Cannot register new forgeItem: " + itemName);
                        continue;
                    }
                    values.add(material);
                    length++;
                    items++;
                    CatServer.LOGGER.debug("Save-Item: " + material.name() + " - " + itemName);
                } catch (Throwable e) {
                    CatServer.LOGGER.error("Cannot register new forgeItem : " + itemName);
                }
            }
            ITEM_MATERIAL.put(item, material);
            MATERIAL_ITEM.put(material, item);
        }
        EnumHelper.addEnums(Material.class, values);
        CatServer.LOGGER.info("Registered {} items into Bukkit", items);
    }

    @Contract("null -> fail")
    public static String standardize(ResourceLocation location) {
        Preconditions.checkNotNull(location, "location");
        return (location.getNamespace().equals(NamespacedKey.MINECRAFT) ? location.getPath() : location.toString())
                .replace(':', '_')
                .replaceAll("\\s+", "_")
                .replaceAll("\\W", "")
                .toUpperCase(Locale.ENGLISH);
    }
}