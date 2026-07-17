package architecture.goldenboughs_lib.common.entiy.projectile

import architecture.goldenboughs_lib.api.AllOpen
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.projectile.ThrowableProjectile
import net.minecraft.world.level.Level

@AllOpen
abstract class BulletEntity : ThrowableProjectile {
	protected constructor(
		entityType: EntityType<out BulletEntity>,
		level: Level
	) : super(entityType, level)

	protected constructor(
		entityType: EntityType<out BulletEntity>,
		x: Double, y: Double, z: Double, level: Level
	) : super(entityType, x, y, z, level)

	protected constructor(
		entityType: EntityType<out BulletEntity>,
		shooter: LivingEntity, level: Level
	) : super(entityType, shooter, level)

	override fun defineSynchedData(builder: SynchedEntityData.Builder) {
	}

	override fun isInWater(): Boolean {
		return false
	}
}
