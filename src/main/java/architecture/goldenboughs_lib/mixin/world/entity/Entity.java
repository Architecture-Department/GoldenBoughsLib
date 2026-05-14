package architecture.goldenboughs_lib.mixin.world.entity;

import architecture.goldenboughs_lib.mixed.IEntityLib;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Entity.class)
public abstract class Entity implements IEntityLib {
}
