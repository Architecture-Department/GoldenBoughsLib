package architecture.goldenboughs_lib.init

import net.minecraft.core.RegistryAccess
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.damagesource.DamageSource
import net.minecraft.world.damagesource.DamageSources
import net.minecraft.world.damagesource.DamageType
import net.minecraft.world.entity.Entity
import net.minecraft.world.phys.Vec3
import org.jetbrains.annotations.Contract

/**
 * 伤害来源
 * 
 * 
 * 关于四色伤害的使用推荐直接使用[architecture.goldenboughs_lib.mixed.IDamageSource] 中的方法
 */
class LibDamageSources(registry: RegistryAccess) : DamageSources(registry) {
	private val physics: DamageSource = source(LibDamageTypes.PHYSICS)
	private val rationality: DamageSource = source(LibDamageTypes.SPIRIT)
	private val erosion: DamageSource = source(LibDamageTypes.EROSION)
	private val theSoul: DamageSource = source(LibDamageTypes.THE_SOUL)
	private val abnormalities: DamageSource = source(LibDamageTypes.ABNORMALITIES)
	private val ego: DamageSource = source(LibDamageTypes.EGO)

	private val melee: DamageSource = source(LibDamageTypes.MELEE)
	private val remote: DamageSource = source(LibDamageTypes.REMOTE)

	fun physics(): DamageSource {
		return physics
	}

	fun rationality(): DamageSource {
		return rationality
	}

	fun erosion(): DamageSource {
		return erosion
	}

	fun theSoul(): DamageSource {
		return theSoul
	}

	fun abnormalities(): DamageSource {
		return abnormalities
	}

	fun melee(): DamageSource {
		return melee
	}

	fun remote(): DamageSource {
		return remote
	}

	fun ego(): DamageSource {
		return ego
	}

	companion object {
		@JvmStatic
		fun meleeDamage(causer: Entity): DamageSource {
			return createDamage(LibDamageTypes.MELEE, causer)
		}

		@JvmStatic
		fun createDamage(damageTypes: ResourceKey<DamageType>, causingEntity: Entity): DamageSource {
			return createDamage(damageTypes, causingEntity, causingEntity, null)
		}

		@JvmOverloads
		@JvmStatic
		fun createDamage(
			damageTypes: ResourceKey<DamageType>,
			directEntity: Entity?,
			causingEntity: Entity,
			damageSourcePosition: Vec3? = null
		): DamageSource {
			return createDamage(
				causingEntity.level().registryAccess(), damageTypes, directEntity, causingEntity, damageSourcePosition
			)
		}

		@JvmStatic
		fun createDamage(
			registryAccess: RegistryAccess,
			damageTypes: ResourceKey<DamageType>,
			directEntity: Entity?,
			causingEntity: Entity?,
			damageSourcePosition: Vec3?
		): DamageSource {
			return DamageSource(
				registryAccess.registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(damageTypes),
				directEntity,
				causingEntity,
				damageSourcePosition
			)
		}

		@JvmStatic
		fun remoteDamage(causer: Entity): DamageSource {
			return createDamage(LibDamageTypes.REMOTE, causer)
		}

		@Contract("_ -> new")
		@JvmStatic
		fun spiritDamage(causer: Entity): DamageSource {
			return createDamage(LibDamageTypes.SPIRIT, causer)
		}

		@JvmStatic
		@Contract("_ -> new")
		fun erosionDamage(causer: Entity): DamageSource {
			return createDamage(LibDamageTypes.EROSION, causer)
		}

		@Contract("_ -> new")
		@JvmStatic
		fun theSoulDamage(causer: Entity): DamageSource {
			return createDamage(LibDamageTypes.THE_SOUL, causer)
		}

		@Contract("_ -> new")
		@JvmStatic
		fun physicsDamage(causer: Entity): DamageSource {
			return createDamage(LibDamageTypes.PHYSICS, causer)
		}

		@Contract("_ -> new")
		@JvmStatic
		fun abnormalitiesDamage(causer: Entity): DamageSource {
			return createDamage(LibDamageTypes.ABNORMALITIES, causer)
		}

		@Contract("_ -> new")
		@JvmStatic
		fun egoDamage(causer: Entity): DamageSource {
			return createDamage(LibDamageTypes.EGO, causer)
		}
	}
}
