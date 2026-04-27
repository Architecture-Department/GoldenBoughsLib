package architecture.goldenboughs_lib.mixin.world.entity;

import architecture.goldenboughs_lib.mixed.ILivingDamageEvent$Post;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingDamageEvent.Post.class)
public abstract class LivingDamageEvent$PostMixin implements ILivingDamageEvent$Post {
	@Unique
	private DamageContainer projectMoon$damageContainer;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void goldenboughs_lib$projectMoon$Post(LivingEntity entity, DamageContainer container, CallbackInfo ci) {
		projectMoon$damageContainer = container;
	}

	@Override
	public DamageContainer goldenboughs_lib$getDamageContainer() {
		return projectMoon$damageContainer;
	}
}
