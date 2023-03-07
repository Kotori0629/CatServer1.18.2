package catserver.server;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class CatServerCaptures {
    private static final CatServerCaptures catServerCaptures = new CatServerCaptures();
    private AtomicReference<Entity> entity = new AtomicReference<>();
    private AtomicReference<CreatureSpawnEvent.SpawnReason> spawnReason = new AtomicReference<>();
    private AtomicBoolean doPlace = new AtomicBoolean(true);
    private AtomicInteger spawnerLimit = new AtomicInteger();
    private AtomicInteger createPortalRadius = new AtomicInteger();
    private AtomicReference<BlockPos> blockPos = new AtomicReference<>();
    private AtomicReference<ItemStack> itemstack = new AtomicReference<>();
    private AtomicReference<PlayerTeleportEvent.TeleportCause> changeDimCause = new AtomicReference<>();
    private AtomicInteger searchPortalRadius = new AtomicInteger();
    private AtomicBoolean canCreatePortal = new AtomicBoolean();
    private AtomicBoolean isForceSleep = new AtomicBoolean(false);
    private AtomicBoolean isCallEvent = new AtomicBoolean(true);
    private AtomicBoolean isSilent = new AtomicBoolean(false);
    private AtomicReference<Direction> direction = new AtomicReference<>();
    private AtomicReference<BlockState> blockState = new AtomicReference<>();

    public void captureEntity(Entity entity) {
        this.entity.set(entity);
    }

    public Entity getCaptureEntity() {
        return this.entity.getAndSet(null);
    }

    public void captureSpawnReason(CreatureSpawnEvent.SpawnReason spawnReason) {
        this.spawnReason.set(spawnReason);
    }

    public void captureSpawnerLimit(int i) {
        this.spawnerLimit.set(i);
    }

    public void capturePortalRadius(int i) {
        this.createPortalRadius.set(i);
    }

    public void captureBlockPos(BlockPos pos) {
        this.blockPos.set(pos);
    }
    public void captureItemStack(ItemStack itemstack) {
        this.itemstack.set(itemstack);
    }
    public void captureDirection(Direction direction) {
        this.direction.set(direction);
    }

    public void captureChangeDimCause(PlayerTeleportEvent.TeleportCause cause) {
        this.changeDimCause.set(cause);
    }

    public void capturePortalSearchRadius(int i) {
        this.searchPortalRadius.set(i);
    }

    public void captureCanCreatePortal(boolean value) {
        this.canCreatePortal.set(value);
    }
    public void captureIsForceSleep(boolean value) {
        this.isForceSleep.set(value);
    }
    public void captureIsCallEvent(boolean value) {
        this.isCallEvent.set(value);
    }
    public void captureIsSlient(boolean isSlient) {
        this.isSilent.set(isSlient);
    }
    public void captureBlockState(BlockState blockState) {
        this.blockState.set(blockState);
    }

    public CreatureSpawnEvent.SpawnReason getCaptureSpawnReason() {
        return this.spawnReason.getAndSet(null);
    }

    public void captureDoPlace(boolean doPlace) {
        this.doPlace.set(doPlace);
    }

    public boolean getCaptureDoPlace() {
        return this.doPlace.getAndSet(true);
    }

    public int getCaptureLimit() {
        return this.spawnerLimit.getAndSet(0);
    }

    public BlockState getCaptureBlockState() {
        return this.blockState.getAndSet(null);
    }

    public int getCapturePortalRadius() {
        return this.createPortalRadius.getAndSet(0);
    }

    public BlockPos getCaptureBlockPos() {
        return this.blockPos.getAndSet(null);
    }

    public ItemStack getCaptureItemStack() {
        return this.itemstack.getAndSet(null);
    }

    public Direction getCaptureDirection() {
        return this.direction.getAndSet(null);
    }

    public PlayerTeleportEvent.TeleportCause getCaptureChangeDimCause() {
        return this.changeDimCause.getAndSet(null);
    }

    public int getCapturePortalSearchRadius() {
        return this.searchPortalRadius.getAndSet(0);
    }

    public boolean getCaptureCanCreatePortal() {
        return this.canCreatePortal.getAndSet(false);
    }

    public boolean getCaptureIsForceSleep() {
        return this.isForceSleep.getAndSet(false);
    }

    public boolean getCaptureIsCallEvent() {
        return this.isCallEvent.getAndSet(true);
    }
    public boolean getCaptureIsSlient() {
        return this.isSilent.getAndSet(false);
    }

    public static CatServerCaptures getCatServerCaptures() {
        return catServerCaptures;
    }
}
