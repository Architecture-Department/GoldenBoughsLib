package architecture.goldenboughs_lib.mixin.world.entity;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import javax.annotation.Nullable;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {
	@Accessor
	int getAttackStrengthTicker();

	@Accessor
	void setAttackStrengthTicker(int attackStrengthTicker);

	@Invoker
	void callOnEffectUpdated(MobEffectInstance effectInstance, boolean forced, @Nullable Entity entity);
}
