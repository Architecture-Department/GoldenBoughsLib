package architecture.goldenboughs_lib.mixin.world.entity;

import architecture.goldenboughs_lib.mixed.ILivingEntityLib;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin implements ILivingEntityLib {
}
