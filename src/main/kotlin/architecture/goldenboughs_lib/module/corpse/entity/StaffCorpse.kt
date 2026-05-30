package architecture.goldenboughs_lib.module.corpse.entity

import net.minecraft.Util
import net.minecraft.core.Direction
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.syncher.EntityDataAccessor
import net.minecraft.network.syncher.EntityDataSerializers
import net.minecraft.network.syncher.SynchedEntityData
import net.minecraft.tags.DamageTypeTags
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageTypes
import net.minecraft.world.entity.EntityType
import net.minecraft.world.entity.Mob
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import java.util.*

class StaffCorpse(
	type: EntityType<out Mob>,
	level: Level
) : Mob(type, level) {
	private fun calculateBoundingBox() {
		val direction = Direction.fromYRot(yRot.toDouble())
		val isXAxis = direction.stepX != 0
		boundingBox = AABB(
			if (isXAxis) -1.0 else -0.5, 0.0, if (isXAxis) -0.5 else -1.0,
			if (isXAxis) 1.0 else 0.5, 0.5, if (isXAxis) 0.5 else 1.0
		).move(x, y, z)
	}

	var ownerUuid: UUID
		get() = entityData.get(OWNER_UUID).orElse(Util.NIL_UUID)!!
		set(ownerUuid) {
			entityData.set(
				OWNER_UUID,
				Optional.ofNullable(ownerUuid)
			)
		}

	var ownerName: String
		get() = entityData.get(OWNER_NAME)
		set(ownerName) {
			entityData.set(
				OWNER_NAME,
				ownerName
			)
		}

	var isOnFace: Boolean
		get() = entityData.get(ON_FACE)
		set(onFace) {
			entityData.set(
				ON_FACE,
				onFace
			)
		}

	override fun tick() {
		super.tick()
		calculateBoundingBox()
	}

	override fun setPos(x: Double, y: Double, z: Double) {
		super.setPos(x, y, z)
		calculateBoundingBox()
	}

	override fun defineSynchedData(entityData: SynchedEntityData.Builder) {
		super.defineSynchedData(entityData)
		entityData.define(OWNER_UUID, Optional.empty<UUID>())
		entityData.define(OWNER_NAME, "")
		entityData.define(ON_FACE, false)
	}

	override fun readAdditionalSaveData(compound: CompoundTag) {
		super.readAdditionalSaveData(compound)
		if (compound.contains("OwnerUUID")) {
			this.ownerUuid = compound.getUUID("OwnerUUID")
		}
		this.ownerName = compound.getString("OwnerName")
		this.isOnFace = compound.getBoolean("OnFace")
	}

	override fun addAdditionalSaveData(compound: CompoundTag) {
		super.addAdditionalSaveData(compound)
		compound.putUUID("OwnerUUID", this.ownerUuid)
		compound.putString("OwnerName", this.ownerName)
		compound.putBoolean("OnFace", this.isOnFace)
	}

	override fun hurt(source: DamageSource, amount: Float): Boolean {
		if (source.`is`(DamageTypeTags.IS_FIRE)
			|| source.`is`(DamageTypes.FELL_OUT_OF_WORLD)
			|| source.`is`(DamageTypes.GENERIC_KILL)
		) {
			return super.hurt(source, amount)
		}
		return false
	}

	override fun isPickable(): Boolean {
		return false
	}

	override fun isPushable(): Boolean {
		return false
	}

	override fun pushEntities() {
	}

	companion object {
		private val OWNER_UUID: EntityDataAccessor<Optional<UUID>> = SynchedEntityData.defineId(
			StaffCorpse::class.java, EntityDataSerializers.OPTIONAL_UUID
		)
		private val OWNER_NAME: EntityDataAccessor<String> =
			SynchedEntityData.defineId(StaffCorpse::class.java, EntityDataSerializers.STRING)
		private val ON_FACE: EntityDataAccessor<Boolean> =
			SynchedEntityData.defineId(StaffCorpse::class.java, EntityDataSerializers.BOOLEAN)
	}
}
