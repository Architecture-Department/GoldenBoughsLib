package architecture.goldenboughs_lib.util

import architecture.goldenboughs_lib.mixin.world.entity.LivingEntityAccessor
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity

fun LivingEntity.getAttackStrengthTicker(): Int {
	return (this as LivingEntityAccessor).getAttackStrengthTicker()
}

fun LivingEntity.setAttackStrengthTicker(attackStrengthTicker: Int) {
	(this as LivingEntityAccessor).setAttackStrengthTicker(attackStrengthTicker)
}

fun LivingEntity.onEffectUpdated(effectInstance: MobEffectInstance, forced: Boolean, entity: Entity?) {
	(this as LivingEntityAccessor).callOnEffectUpdated(effectInstance, forced, entity)
}