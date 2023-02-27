package catserver.server;

import net.minecraft.world.entity.Entity;
import org.bukkit.event.entity.CreatureSpawnEvent;

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

    public int getCapturePortalRadius() {
        return this.createPortalRadius.getAndSet(0);
    }

    public static CatServerCaptures getCatServerCaptures() {
        return catServerCaptures;
    }
}
