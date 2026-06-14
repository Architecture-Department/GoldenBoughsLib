package architecture.goldenboughs_lib.init

import architecture.goldenboughs_lib.datagen.i18n.LibZhCn
import architecture.goldenboughs_lib.module.corpse.entity.StaffCorpse
import architecture.goldenboughs_lib.module.lc_damage.api.LcLevel
import architecture.goldenboughs_lib.module.lc_damage.util.LcLevelUtil
import architecture.goldenboughs_lib.util.LibUtil
import net.minecraft.core.registries.Registries
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.MobCategory
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object LibEntityTypes {
	@JvmField
	val REGISTRY: DeferredRegister<EntityType<*>> = DeferredRegister.create(Registries.ENTITY_TYPE, LibUtil.ID)

	@JvmField
	val STAFF_CORPSE: DeferredHolder<EntityType<*>, EntityType<StaffCorpse>> = register<StaffCorpse>(
		"staff_corpse",
		"职员尸体",
		LcLevel.ZAYIN,
		EntityType.Builder.of(::StaffCorpse, MobCategory.MISC)
			.clientTrackingRange(128)
			.setUpdateInterval(1)
			.setShouldReceiveVelocityUpdates(true)
			.sized(2f, 0.5f)
			.eyeHeight(0.25f)
	)

	private fun <I : Entity> register(
		name: String, zhName: String, lcLevel: LcLevel, sup: EntityType.Builder<I>
	): DeferredHolder<EntityType<*>, EntityType<I>> {
		return register(name, zhName, lcLevel) { sup.build(name) }
	}

	private fun <T : Entity> register(
		name: String, zhName: String, lcLevel: LcLevel, sup: Supplier<EntityType<T>>
	): DeferredHolder<EntityType<*>, EntityType<T>> {
		val holder = REGISTRY.register(name, sup)
		LcLevelUtil.lcLevel(lcLevel, holder)
		LibZhCn.addI18nEntityTypeText(zhName, holder)
		return holder
	}
}
