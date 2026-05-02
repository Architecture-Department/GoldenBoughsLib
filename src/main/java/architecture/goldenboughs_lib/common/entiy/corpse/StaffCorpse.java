package architecture.goldenboughs_lib.common.entiy.corpse;

import architecture.goldenboughs_lib.init.LibEntityTypes;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

@EventBusSubscriber
public class StaffCorpse extends Mob {
	private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(StaffCorpse.class, EntityDataSerializers.OPTIONAL_UUID);
	private static final EntityDataAccessor<String> OWNER_NAME = SynchedEntityData.defineId(StaffCorpse.class, EntityDataSerializers.STRING);
	private static final EntityDataAccessor<Boolean> ON_FACE = SynchedEntityData.defineId(StaffCorpse.class, EntityDataSerializers.BOOLEAN);

	public StaffCorpse(EntityType<? extends Mob> type, Level level) {
		super(type, level);
	}

	@SubscribeEvent
	public static void livingDeath(LivingDeathEvent event) {
		if (event.getEntity() instanceof ServerPlayer serverPlayer) {
			StaffCorpse staffCorpse = new StaffCorpse(LibEntityTypes.STAFF_CORPSE.get(), serverPlayer.level());
			staffCorpse.setOwnerUuid(serverPlayer.getUUID());
			staffCorpse.setOwnerName(serverPlayer.getDisplayName().getString());
			boolean onFace = serverPlayer.getRandom().nextBoolean();
			staffCorpse.setOnFace(onFace);
			staffCorpse.setPos(serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ());
			staffCorpse.setYRot(onFace ? serverPlayer.getYRot() : -serverPlayer.getYRot());
			staffCorpse.setYHeadRot(staffCorpse.getYRot());
			staffCorpse.setYBodyRot(staffCorpse.getYRot());
			serverPlayer.level().addFreshEntity(staffCorpse);
		}
	}

	private void calculateBoundingBox() {
		Direction direction = Direction.fromYRot(getYRot());
		boolean isXAxis = direction.getStepX() != 0;
		setBoundingBox(new AABB(
			isXAxis ? -1 : -0.5, 0, isXAxis ? -0.5 : -1,
			isXAxis ? 1 : 0.5, 0.5, isXAxis ? 0.5 : 1
		).move(getX(), getY(), getZ()));
	}

	public UUID getOwnerUuid() {
		return entityData.get(OWNER_UUID).orElse(Util.NIL_UUID);
	}

	public void setOwnerUuid(UUID ownerUuid) {
		entityData.set(OWNER_UUID, Optional.ofNullable(ownerUuid));
	}

	public String getOwnerName() {
		return entityData.get(OWNER_NAME);
	}

	public void setOwnerName(String ownerName) {
		entityData.set(OWNER_NAME, ownerName);
	}

	public boolean isOnFace() {
		return entityData.get(ON_FACE);
	}

	public void setOnFace(boolean onFace) {
		entityData.set(ON_FACE, onFace);
	}

	@Override
	public void tick() {
		super.tick();
		calculateBoundingBox();
	}

	@Override
	public void setPos(double x, double y, double z) {
		super.setPos(x, y, z);
		calculateBoundingBox();
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.@NonNull Builder entityData) {
		super.defineSynchedData(entityData);
		entityData.define(OWNER_UUID, Optional.empty());
		entityData.define(OWNER_NAME, "");
		entityData.define(ON_FACE, false);
	}

	@Override
	public void readAdditionalSaveData(@NotNull CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		if (compound.contains("OwnerUUID")) {
			setOwnerUuid(compound.getUUID("OwnerUUID"));
		}
		setOwnerName(compound.getString("OwnerName"));
		setOnFace(compound.getBoolean("OnFace"));
	}

	@Override
	public void addAdditionalSaveData(@NotNull CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		compound.putUUID("OwnerUUID", getOwnerUuid());
		compound.putString("OwnerName", getOwnerName());
		compound.putBoolean("OnFace", isOnFace());
	}

	@Override
	public boolean hurt(@NotNull DamageSource source, float amount) {
		if (source.is(DamageTypeTags.IS_FIRE)
			|| source.is(DamageTypes.FELL_OUT_OF_WORLD)
			|| source.is(DamageTypes.GENERIC_KILL)) {
			return super.hurt(source, amount);
		}
		return false;
	}

	@Override
	public boolean isPickable() {
		return false;
	}

	@Override
	public boolean isPushable() {
		return false;
	}

	@Override
	protected void pushEntities() {
	}
}
