package architecture.goldenboughs_lib.common.entiy.projectile;

import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;

public abstract class BulletEntity extends ThrowableProjectile {
	protected BulletEntity(EntityType<? extends BulletEntity> entityType, Level level) {
		super(entityType, level);
	}

	protected BulletEntity(EntityType<? extends BulletEntity> entityType,
	                       double x, double y, double z, Level level) {
		super(entityType, x, y, z, level);
	}

	protected BulletEntity(EntityType<? extends BulletEntity> entityType,
	                       LivingEntity shooter, Level level) {
		super(entityType, shooter, level);
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
	}

	@Override
	public boolean isInWater() {
		return false;
	}
}
