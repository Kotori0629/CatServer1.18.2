package catserver.server;

import net.minecraft.world.entity.Entity;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.concurrent.atomic.AtomicReference;

public class CatServerCaptures {
    private static final CatServerCaptures catServerCaptures = new CatServerCaptures();
    private AtomicReference<Entity> entity = new AtomicReference<>();
    private AtomicReference<CreatureSpawnEvent.SpawnReason> spawnReason = new AtomicReference<>();

    public void captureEntity(Entity entity) {
        this.entity.set(entity);
    }

    public Entity getCaptureEntity() {
        return this.entity.getAndSet(null);
    }

    public void captureSpawnReason(CreatureSpawnEvent.SpawnReason spawnReason) {
        this.spawnReason.set(spawnReason);
    }

    public CreatureSpawnEvent.SpawnReason getCaptureSpawnReason() {
        return this.spawnReason.getAndSet(null);
    }

    public static CatServerCaptures getCatServerCaptures() {
        return catServerCaptures;
    }
}
