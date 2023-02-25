package catserver.server;

import net.minecraft.world.entity.Entity;
import java.util.concurrent.atomic.AtomicReference;

public class CatServerCaptures {
    private static final CatServerCaptures catServerCaptures = new CatServerCaptures();
    private AtomicReference<Entity> entity;

    public void captureEntity(Entity entity) {
        this.entity.set(entity);
    }

    public Entity getCaptureEntity() {
        return this.entity.getAndSet(null);
    }

    public static CatServerCaptures getCatServerCaptures() {
        return catServerCaptures;
    }
}
