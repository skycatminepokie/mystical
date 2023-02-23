package skycat.mystical.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import skycat.mystical.Mystical;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin { // TODO Test


    /**
     * @author skycatminepokie
     * @reason Make entity go zoom
     */
    @Overwrite
    public void pullHookedEntity(Entity entity) {
        FishingBobberEntity dis = ((FishingBobberEntity) (Object) this);
        Entity entity2 = dis.getOwner();
        if (entity2 == null) {
            return;
        }
        Vec3d vec3d = new Vec3d(entity2.getX() - dis.getX(), entity2.getY() - dis.getY(), entity2.getZ() - dis.getZ()).multiply(0.5);
        if (Mystical.SPELL_HANDLER.shouldDoFishingRodLaunch()) {
            vec3d = vec3d.multiply(2.0); // TODO: config
        }
        entity.setVelocity(entity.getVelocity().add(vec3d));
    }
}
