package org.bukkit.craftbukkit.v1_18_R2.block.data;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.commands.arguments.blocks.ArgumentBlock;
import net.minecraft.core.EnumDirection;
import net.minecraft.core.IRegistry;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.INamable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.IBlockData;
import net.minecraft.world.level.block.state.IBlockDataHolder;
import net.minecraft.world.level.block.state.properties.BlockStateBoolean;
import net.minecraft.world.level.block.state.properties.BlockStateEnum;
import net.minecraft.world.level.block.state.properties.BlockStateInteger;
import net.minecraft.world.level.block.state.properties.IBlockState;
import org.bukkit.Material;
import org.bukkit.SoundGroup;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_18_R2.CraftSoundGroup;
import org.bukkit.craftbukkit.v1_18_R2.block.CraftBlock;
import org.bukkit.craftbukkit.v1_18_R2.util.CraftMagicNumbers;
import org.bukkit.craftbukkit.v1_18_R2.block.impl.*;
import org.bukkit.craftbukkit.v1_18_R2.block.impl.CraftRotatable;

public class CraftBlockData implements BlockData {

    private IBlockData state;
    private Map<IBlockState<?>, Comparable<?>> parsedStates;

    protected CraftBlockData() {
        throw new AssertionError("Template Constructor");
    }

    protected CraftBlockData(IBlockData state) {
        this.state = state;
    }

    @Override
    public Material getMaterial() {
        return CraftMagicNumbers.getMaterial(state.getBlock());
    }

    public IBlockData getState() {
        return state;
    }

    /**
     * Get a given BlockStateEnum's value as its Bukkit counterpart.
     *
     * @param nms the NMS state to convert
     * @param bukkit the Bukkit class
     * @param <B> the type
     * @return the matching Bukkit type
     */
    protected <B extends Enum<B>> B get(BlockStateEnum<?> nms, Class<B> bukkit) {
        return toBukkit(state.getValue(nms), bukkit);
    }

    /**
     * Convert all values from the given BlockStateEnum to their appropriate
     * Bukkit counterpart.
     *
     * @param nms the NMS state to get values from
     * @param bukkit the bukkit class to convert the values to
     * @param <B> the bukkit class type
     * @return an immutable Set of values in their appropriate Bukkit type
     */
    @SuppressWarnings("unchecked")
    protected <B extends Enum<B>> Set<B> getValues(BlockStateEnum<?> nms, Class<B> bukkit) {
        ImmutableSet.Builder<B> values = ImmutableSet.builder();

        for (Enum<?> e : nms.getPossibleValues()) {
            values.add(toBukkit(e, bukkit));
        }

        return values.build();
    }

    /**
     * Set a given {@link BlockStateEnum} with the matching enum from Bukkit.
     *
     * @param nms the NMS BlockStateEnum to set
     * @param bukkit the matching Bukkit Enum
     * @param <B> the Bukkit type
     * @param <N> the NMS type
     */
    protected <B extends Enum<B>, N extends Enum<N> & INamable> void set(BlockStateEnum<N> nms, Enum<B> bukkit) {
        this.parsedStates = null;
        this.state = this.state.setValue(nms, toNMS(bukkit, nms.getValueClass()));
    }

    @Override
    public BlockData merge(BlockData data) {
        CraftBlockData craft = (CraftBlockData) data;
        Preconditions.checkArgument(craft.parsedStates != null, "Data not created via string parsing");
        Preconditions.checkArgument(this.state.getBlock() == craft.state.getBlock(), "States have different types (got %s, expected %s)", data, this);

        CraftBlockData clone = (CraftBlockData) this.clone();
        clone.parsedStates = null;

        for (IBlockState parsed : craft.parsedStates.keySet()) {
            clone.state = clone.state.setValue(parsed, craft.state.getValue(parsed));
        }

        return clone;
    }

    @Override
    public boolean matches(BlockData data) {
        if (data == null) {
            return false;
        }
        if (!(data instanceof CraftBlockData)) {
            return false;
        }

        CraftBlockData craft = (CraftBlockData) data;
        if (this.state.getBlock() != craft.state.getBlock()) {
            return false;
        }

        // Fastpath an exact match
        boolean exactMatch = this.equals(data);

        // If that failed, do a merge and check
        if (!exactMatch && craft.parsedStates != null) {
            return this.merge(data).equals(this);
        }

        return exactMatch;
    }

    private static final Map<Class<? extends Enum<?>>, Enum<?>[]> ENUM_VALUES = new HashMap<>();

    /**
     * Convert an NMS Enum (usually a BlockStateEnum) to its appropriate Bukkit
     * enum from the given class.
     *
     * @throws IllegalStateException if the Enum could not be converted
     */
    @SuppressWarnings("unchecked")
    private static <B extends Enum<B>> B toBukkit(Enum<?> nms, Class<B> bukkit) {
        if (nms instanceof EnumDirection) {
            return (B) CraftBlock.notchToBlockFace((EnumDirection) nms);
        }
        return (B) ENUM_VALUES.computeIfAbsent(bukkit, Class::getEnumConstants)[nms.ordinal()];
    }

    /**
     * Convert a given Bukkit enum to its matching NMS enum type.
     *
     * @param bukkit the Bukkit enum to convert
     * @param nms the NMS class
     * @return the matching NMS type
     * @throws IllegalStateException if the Enum could not be converted
     */
    @SuppressWarnings("unchecked")
    private static <N extends Enum<N> & INamable> N toNMS(Enum<?> bukkit, Class<N> nms) {
        if (bukkit instanceof BlockFace) {
            return (N) CraftBlock.blockFaceToNotch((BlockFace) bukkit);
        }
        return (N) ENUM_VALUES.computeIfAbsent(nms, Class::getEnumConstants)[bukkit.ordinal()];
    }

    /**
     * Get the current value of a given state.
     *
     * @param ibs the state to check
     * @param <T> the type
     * @return the current value of the given state
     */
    protected <T extends Comparable<T>> T get(IBlockState<T> ibs) {
        // Straight integer or boolean getter
        return this.state.getValue(ibs);
    }

    /**
     * Set the specified state's value.
     *
     * @param ibs the state to set
     * @param v the new value
     * @param <T> the state's type
     * @param <V> the value's type. Must match the state's type.
     */
    public <T extends Comparable<T>, V extends T> void set(IBlockState<T> ibs, V v) {
        // Straight integer or boolean setter
        this.parsedStates = null;
        this.state = this.state.setValue(ibs, v);
    }

    @Override
    public String getAsString() {
        return toString(state.getValues());
    }

    @Override
    public String getAsString(boolean hideUnspecified) {
        return (hideUnspecified && parsedStates != null) ? toString(parsedStates) : getAsString();
    }

    @Override
    public BlockData clone() {
        try {
            return (BlockData) super.clone();
        } catch (CloneNotSupportedException ex) {
            throw new AssertionError("Clone not supported", ex);
        }
    }

    @Override
    public String toString() {
        return "CraftBlockData{" + getAsString() + "}";
    }

    // Mimicked from BlockDataAbstract#toString()
    public String toString(Map<IBlockState<?>, Comparable<?>> states) {
        StringBuilder stateString = new StringBuilder(IRegistry.BLOCK.getKey(state.getBlock()).toString());

        if (!states.isEmpty()) {
            stateString.append('[');
            stateString.append(states.entrySet().stream().map(IBlockDataHolder.PROPERTY_ENTRY_TO_STRING_FUNCTION).collect(Collectors.joining(",")));
            stateString.append(']');
        }

        return stateString.toString();
    }

    public NBTTagCompound toStates() {
        NBTTagCompound compound = new NBTTagCompound();

        for (Map.Entry<IBlockState<?>, Comparable<?>> entry : state.getValues().entrySet()) {
            IBlockState iblockstate = (IBlockState) entry.getKey();

            compound.putString(iblockstate.getName(), iblockstate.getName(entry.getValue()));
        }

        return compound;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CraftBlockData && state.equals(((CraftBlockData) obj).state);
    }

    @Override
    public int hashCode() {
        return state.hashCode();
    }

    protected static BlockStateBoolean getBoolean(String name) {
        throw new AssertionError("Template Method");
    }

    protected static BlockStateBoolean getBoolean(String name, boolean optional) {
        throw new AssertionError("Template Method");
    }

    protected static BlockStateEnum<?> getEnum(String name) {
        throw new AssertionError("Template Method");
    }

    protected static BlockStateInteger getInteger(String name) {
        throw new AssertionError("Template Method");
    }

    protected static BlockStateBoolean getBoolean(Class<? extends Block> block, String name) {
        return (BlockStateBoolean) getState(block, name, false);
    }

    protected static BlockStateBoolean getBoolean(Class<? extends Block> block, String name, boolean optional) {
        return (BlockStateBoolean) getState(block, name, optional);
    }

    protected static BlockStateEnum<?> getEnum(Class<? extends Block> block, String name) {
        return (BlockStateEnum<?>) getState(block, name, false);
    }

    protected static BlockStateInteger getInteger(Class<? extends Block> block, String name) {
        return (BlockStateInteger) getState(block, name, false);
    }

    /**
     * Get a specified {@link IBlockState} from a given block's class with a
     * given name
     *
     * @param block the class to retrieve the state from
     * @param name the name of the state to retrieve
     * @param optional if the state can be null
     * @return the specified state or null
     * @throws IllegalStateException if the state is null and {@code optional}
     * is false.
     */
    private static IBlockState<?> getState(Class<? extends Block> block, String name, boolean optional) {
        IBlockState<?> state = null;

        for (Block instance : IRegistry.BLOCK) {
            if (instance.getClass() == block) {
                if (state == null) {
                    state = instance.getStateDefinition().getProperty(name);
                } else {
                    IBlockState<?> newState = instance.getStateDefinition().getProperty(name);

                    Preconditions.checkState(state == newState, "State mistmatch %s,%s", state, newState);
                }
            }
        }

        Preconditions.checkState(optional || state != null, "Null state for %s,%s", block, name);

        return state;
    }

    /**
     * Get the minimum value allowed by the BlockStateInteger.
     *
     * @param state the state to check
     * @return the minimum value allowed
     */
    protected static int getMin(BlockStateInteger state) {
        return state.min;
    }

    /**
     * Get the maximum value allowed by the BlockStateInteger.
     *
     * @param state the state to check
     * @return the maximum value allowed
     */
    protected static int getMax(BlockStateInteger state) {
        return state.max;
    }

    //
    private static final Map<Class<? extends Block>, Function<IBlockData, CraftBlockData>> MAP = new HashMap<>();

    static {
        //<editor-fold desc="CraftBlockData Registration" defaultstate="collapsed">
        register(net.minecraft.world.level.block.AmethystClusterBlock.class, CraftAmethystCluster::new);
        register(net.minecraft.world.level.block.BigDripleafBlock.class, CraftBigDripleaf::new);
        register(net.minecraft.world.level.block.BigDripleafStemBlock.class, CraftBigDripleafStem::new);
        register(net.minecraft.world.level.block.BlockAnvil.class, CraftAnvil::new);
        register(net.minecraft.world.level.block.BlockBamboo.class, CraftBamboo::new);
        register(net.minecraft.world.level.block.BlockBanner.class, CraftBanner::new);
        register(net.minecraft.world.level.block.BlockBannerWall.class, CraftBannerWall::new);
        register(net.minecraft.world.level.block.BlockBarrel.class, CraftBarrel::new);
        register(net.minecraft.world.level.block.BlockBed.class, CraftBed::new);
        register(net.minecraft.world.level.block.BlockBeehive.class, CraftBeehive::new);
        register(net.minecraft.world.level.block.BlockBeetroot.class, CraftBeetroot::new);
        register(net.minecraft.world.level.block.BlockBell.class, CraftBell::new);
        register(net.minecraft.world.level.block.BlockBlastFurnace.class, CraftBlastFurnace::new);
        register(net.minecraft.world.level.block.BlockBrewingStand.class, CraftBrewingStand::new);
        register(net.minecraft.world.level.block.BlockBubbleColumn.class, CraftBubbleColumn::new);
        register(net.minecraft.world.level.block.BlockCactus.class, CraftCactus::new);
        register(net.minecraft.world.level.block.BlockCake.class, CraftCake::new);
        register(net.minecraft.world.level.block.BlockCampfire.class, CraftCampfire::new);
        register(net.minecraft.world.level.block.BlockCarrots.class, CraftCarrots::new);
        register(net.minecraft.world.level.block.BlockChain.class, CraftChain::new);
        register(net.minecraft.world.level.block.BlockChest.class, CraftChest::new);
        register(net.minecraft.world.level.block.BlockChestTrapped.class, CraftChestTrapped::new);
        register(net.minecraft.world.level.block.BlockChorusFlower.class, CraftChorusFlower::new);
        register(net.minecraft.world.level.block.BlockChorusFruit.class, CraftChorusFruit::new);
        register(net.minecraft.world.level.block.BlockCobbleWall.class, CraftCobbleWall::new);
        register(net.minecraft.world.level.block.BlockCocoa.class, CraftCocoa::new);
        register(net.minecraft.world.level.block.BlockCommand.class, CraftCommand::new);
        register(net.minecraft.world.level.block.BlockComposter.class, CraftComposter::new);
        register(net.minecraft.world.level.block.BlockConduit.class, CraftConduit::new);
        register(net.minecraft.world.level.block.BlockCoralDead.class, CraftCoralDead::new);
        register(net.minecraft.world.level.block.BlockCoralFan.class, CraftCoralFan::new);
        register(net.minecraft.world.level.block.BlockCoralFanAbstract.class, CraftCoralFanAbstract::new);
        register(net.minecraft.world.level.block.BlockCoralFanWall.class, CraftCoralFanWall::new);
        register(net.minecraft.world.level.block.BlockCoralFanWallAbstract.class, CraftCoralFanWallAbstract::new);
        register(net.minecraft.world.level.block.BlockCoralPlant.class, CraftCoralPlant::new);
        register(net.minecraft.world.level.block.BlockCrops.class, CraftCrops::new);
        register(net.minecraft.world.level.block.BlockDaylightDetector.class, CraftDaylightDetector::new);
        register(net.minecraft.world.level.block.BlockDirtSnow.class, CraftDirtSnow::new);
        register(net.minecraft.world.level.block.BlockDispenser.class, CraftDispenser::new);
        register(net.minecraft.world.level.block.BlockDoor.class, CraftDoor::new);
        register(net.minecraft.world.level.block.BlockDropper.class, CraftDropper::new);
        register(net.minecraft.world.level.block.BlockEndRod.class, CraftEndRod::new);
        register(net.minecraft.world.level.block.BlockEnderChest.class, CraftEnderChest::new);
        register(net.minecraft.world.level.block.BlockEnderPortalFrame.class, CraftEnderPortalFrame::new);
        register(net.minecraft.world.level.block.BlockFence.class, CraftFence::new);
        register(net.minecraft.world.level.block.BlockFenceGate.class, CraftFenceGate::new);
        register(net.minecraft.world.level.block.BlockFire.class, CraftFire::new);
        register(net.minecraft.world.level.block.BlockFloorSign.class, CraftFloorSign::new);
        register(net.minecraft.world.level.block.BlockFluids.class, CraftFluids::new);
        register(net.minecraft.world.level.block.BlockFurnaceFurace.class, CraftFurnaceFurace::new);
        register(net.minecraft.world.level.block.BlockGlazedTerracotta.class, CraftGlazedTerracotta::new);
        register(net.minecraft.world.level.block.BlockGrass.class, CraftGrass::new);
        register(net.minecraft.world.level.block.BlockGrindstone.class, CraftGrindstone::new);
        register(net.minecraft.world.level.block.BlockHay.class, CraftHay::new);
        register(net.minecraft.world.level.block.BlockHopper.class, CraftHopper::new);
        register(net.minecraft.world.level.block.BlockHugeMushroom.class, CraftHugeMushroom::new);
        register(net.minecraft.world.level.block.BlockIceFrost.class, CraftIceFrost::new);
        register(net.minecraft.world.level.block.BlockIronBars.class, CraftIronBars::new);
        register(net.minecraft.world.level.block.BlockJigsaw.class, CraftJigsaw::new);
        register(net.minecraft.world.level.block.BlockJukeBox.class, CraftJukeBox::new);
        register(net.minecraft.world.level.block.BlockKelp.class, CraftKelp::new);
        register(net.minecraft.world.level.block.BlockLadder.class, CraftLadder::new);
        register(net.minecraft.world.level.block.BlockLantern.class, CraftLantern::new);
        register(net.minecraft.world.level.block.BlockLeaves.class, CraftLeaves::new);
        register(net.minecraft.world.level.block.BlockLectern.class, CraftLectern::new);
        register(net.minecraft.world.level.block.BlockLever.class, CraftLever::new);
        register(net.minecraft.world.level.block.BlockLoom.class, CraftLoom::new);
        register(net.minecraft.world.level.block.BlockMinecartDetector.class, CraftMinecartDetector::new);
        register(net.minecraft.world.level.block.BlockMinecartTrack.class, CraftMinecartTrack::new);
        register(net.minecraft.world.level.block.BlockMycel.class, CraftMycel::new);
        register(net.minecraft.world.level.block.BlockNetherWart.class, CraftNetherWart::new);
        register(net.minecraft.world.level.block.BlockNote.class, CraftNote::new);
        register(net.minecraft.world.level.block.BlockObserver.class, CraftObserver::new);
        register(net.minecraft.world.level.block.BlockPortal.class, CraftPortal::new);
        register(net.minecraft.world.level.block.BlockPotatoes.class, CraftPotatoes::new);
        register(net.minecraft.world.level.block.BlockPoweredRail.class, CraftPoweredRail::new);
        register(net.minecraft.world.level.block.BlockPressurePlateBinary.class, CraftPressurePlateBinary::new);
        register(net.minecraft.world.level.block.BlockPressurePlateWeighted.class, CraftPressurePlateWeighted::new);
        register(net.minecraft.world.level.block.BlockPumpkinCarved.class, CraftPumpkinCarved::new);
        register(net.minecraft.world.level.block.BlockRedstoneComparator.class, CraftRedstoneComparator::new);
        register(net.minecraft.world.level.block.BlockRedstoneLamp.class, CraftRedstoneLamp::new);
        register(net.minecraft.world.level.block.BlockRedstoneOre.class, CraftRedstoneOre::new);
        register(net.minecraft.world.level.block.BlockRedstoneTorch.class, CraftRedstoneTorch::new);
        register(net.minecraft.world.level.block.BlockRedstoneTorchWall.class, CraftRedstoneTorchWall::new);
        register(net.minecraft.world.level.block.BlockRedstoneWire.class, CraftRedstoneWire::new);
        register(net.minecraft.world.level.block.BlockReed.class, CraftReed::new);
        register(net.minecraft.world.level.block.BlockRepeater.class, CraftRepeater::new);
        register(net.minecraft.world.level.block.BlockRespawnAnchor.class, CraftRespawnAnchor::new);
        register(net.minecraft.world.level.block.BlockRotatable.class, CraftRotatable::new);
        register(net.minecraft.world.level.block.BlockSapling.class, CraftSapling::new);
        register(net.minecraft.world.level.block.BlockScaffolding.class, CraftScaffolding::new);
        register(net.minecraft.world.level.block.BlockSeaPickle.class, CraftSeaPickle::new);
        register(net.minecraft.world.level.block.BlockShulkerBox.class, CraftShulkerBox::new);
        register(net.minecraft.world.level.block.BlockSkull.class, CraftSkull::new);
        register(net.minecraft.world.level.block.BlockSkullPlayer.class, CraftSkullPlayer::new);
        register(net.minecraft.world.level.block.BlockSkullPlayerWall.class, CraftSkullPlayerWall::new);
        register(net.minecraft.world.level.block.BlockSkullWall.class, CraftSkullWall::new);
        register(net.minecraft.world.level.block.BlockSmoker.class, CraftSmoker::new);
        register(net.minecraft.world.level.block.BlockSnow.class, CraftSnow::new);
        register(net.minecraft.world.level.block.BlockSoil.class, CraftSoil::new);
        register(net.minecraft.world.level.block.BlockStainedGlassPane.class, CraftStainedGlassPane::new);
        register(net.minecraft.world.level.block.BlockStairs.class, CraftStairs::new);
        register(net.minecraft.world.level.block.BlockStem.class, CraftStem::new);
        register(net.minecraft.world.level.block.BlockStemAttached.class, CraftStemAttached::new);
        register(net.minecraft.world.level.block.BlockStepAbstract.class, CraftStepAbstract::new);
        register(net.minecraft.world.level.block.BlockStoneButton.class, CraftStoneButton::new);
        register(net.minecraft.world.level.block.BlockStonecutter.class, CraftStonecutter::new);
        register(net.minecraft.world.level.block.BlockStructure.class, CraftStructure::new);
        register(net.minecraft.world.level.block.BlockSweetBerryBush.class, CraftSweetBerryBush::new);
        register(net.minecraft.world.level.block.BlockTNT.class, CraftTNT::new);
        register(net.minecraft.world.level.block.BlockTallPlant.class, CraftTallPlant::new);
        register(net.minecraft.world.level.block.BlockTallPlantFlower.class, CraftTallPlantFlower::new);
        register(net.minecraft.world.level.block.BlockTarget.class, CraftTarget::new);
        register(net.minecraft.world.level.block.BlockTorchWall.class, CraftTorchWall::new);
        register(net.minecraft.world.level.block.BlockTrapdoor.class, CraftTrapdoor::new);
        register(net.minecraft.world.level.block.BlockTripwire.class, CraftTripwire::new);
        register(net.minecraft.world.level.block.BlockTripwireHook.class, CraftTripwireHook::new);
        register(net.minecraft.world.level.block.BlockTurtleEgg.class, CraftTurtleEgg::new);
        register(net.minecraft.world.level.block.BlockTwistingVines.class, CraftTwistingVines::new);
        register(net.minecraft.world.level.block.BlockVine.class, CraftVine::new);
        register(net.minecraft.world.level.block.BlockWallSign.class, CraftWallSign::new);
        register(net.minecraft.world.level.block.BlockWeepingVines.class, CraftWeepingVines::new);
        register(net.minecraft.world.level.block.BlockWitherSkull.class, CraftWitherSkull::new);
        register(net.minecraft.world.level.block.BlockWitherSkullWall.class, CraftWitherSkullWall::new);
        register(net.minecraft.world.level.block.BlockWoodButton.class, CraftWoodButton::new);
        register(net.minecraft.world.level.block.CandleBlock.class, CraftCandle::new);
        register(net.minecraft.world.level.block.CandleCakeBlock.class, CraftCandleCake::new);
        register(net.minecraft.world.level.block.CaveVinesBlock.class, CraftCaveVines::new);
        register(net.minecraft.world.level.block.CaveVinesPlantBlock.class, CraftCaveVinesPlant::new);
        register(net.minecraft.world.level.block.GlowLichenBlock.class, CraftGlowLichen::new);
        register(net.minecraft.world.level.block.HangingRootsBlock.class, CraftHangingRoots::new);
        register(net.minecraft.world.level.block.InfestedRotatedPillarBlock.class, CraftInfestedRotatedPillar::new);
        register(net.minecraft.world.level.block.LayeredCauldronBlock.class, CraftLayeredCauldron::new);
        register(net.minecraft.world.level.block.LightBlock.class, CraftLight::new);
        register(net.minecraft.world.level.block.LightningRodBlock.class, CraftLightningRod::new);
        register(net.minecraft.world.level.block.PointedDripstoneBlock.class, CraftPointedDripstone::new);
        register(net.minecraft.world.level.block.PowderSnowCauldronBlock.class, CraftPowderSnowCauldron::new);
        register(net.minecraft.world.level.block.SculkSensorBlock.class, CraftSculkSensor::new);
        register(net.minecraft.world.level.block.SmallDripleafBlock.class, CraftSmallDripleaf::new);
        register(net.minecraft.world.level.block.TallSeagrassBlock.class, CraftTallSeagrass::new);
        register(net.minecraft.world.level.block.WeatheringCopperSlabBlock.class, CraftWeatheringCopperSlab::new);
        register(net.minecraft.world.level.block.WeatheringCopperStairBlock.class, CraftWeatheringCopperStair::new);
        register(net.minecraft.world.level.block.piston.BlockPiston.class, CraftPiston::new);
        register(net.minecraft.world.level.block.piston.BlockPistonExtension.class, CraftPistonExtension::new);
        register(net.minecraft.world.level.block.piston.BlockPistonMoving.class, CraftPistonMoving::new);
        //</editor-fold>
    }

    private static void register(Class<? extends Block> nms, Function<IBlockData, CraftBlockData> bukkit) {
        Preconditions.checkState(MAP.put(nms, bukkit) == null, "Duplicate mapping %s->%s", nms, bukkit);
    }

    public static CraftBlockData newData(Material material, String data) {
        Preconditions.checkArgument(material == null || material.isBlock(), "Cannot get data for not block %s", material);

        IBlockData blockData;
        Block block = CraftMagicNumbers.getBlock(material);
        Map<IBlockState<?>, Comparable<?>> parsed = null;

        // Data provided, use it
        if (data != null) {
            try {
                // Material provided, force that material in
                if (block != null) {
                    data = IRegistry.BLOCK.getKey(block) + data;
                }

                StringReader reader = new StringReader(data);
                ArgumentBlock arg = new ArgumentBlock(reader, false).parse(false);
                Preconditions.checkArgument(!reader.canRead(), "Spurious trailing data: " + data);

                blockData = arg.getState();
                parsed = arg.getProperties();
            } catch (CommandSyntaxException ex) {
                throw new IllegalArgumentException("Could not parse data: " + data, ex);
            }
        } else {
            blockData = block.defaultBlockState();
        }

        CraftBlockData craft = fromData(blockData);
        craft.parsedStates = parsed;
        return craft;
    }

    public static CraftBlockData fromData(IBlockData data) {
        return MAP.getOrDefault(data.getBlock().getClass(), CraftBlockData::new).apply(data);
    }

    @Override
    public SoundGroup getSoundGroup() {
        return CraftSoundGroup.getSoundGroup(state.getSoundType());
    }
}
