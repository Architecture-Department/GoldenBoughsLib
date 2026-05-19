package architecture.goldenboughs_lib.mixin.world.entity;

import architecture.goldenboughs_lib.mixed.IEntityLib;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Entity.class)
public abstract class EntityMixin implements IEntityLib {
}
