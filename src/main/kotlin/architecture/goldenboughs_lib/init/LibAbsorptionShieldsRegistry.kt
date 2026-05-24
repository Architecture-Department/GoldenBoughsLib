package architecture.goldenboughs_lib.init

import architecture.goldenboughs_lib.config.LibConfig
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.effect.MobEffect
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.attachment.AttachmentType
import java.util.function.BiFunction
import java.util.function.Supplier

/**
 * 用于注册单一类型伤害吸收护盾
 * 
 * 
 * 护盾添加方法：
 * 1.注册新的护盾效果
 * 2.注册LibAttachments
 * 3.在此注册即可
 * 
 * 
 * 
 * 2026/3/18 尘昨暄
 */
object LibAbsorptionShieldsRegistry {
	@JvmField
	val shields: MutableList<ShieldEntry> = arrayListOf()

	fun register() {
		register(
			LibMobEffects.PHYSIC_ABSORPTION_SHIELD,
			LibDamageTypes.PHYSICS.location(),
			LibAttachmentTypes.PHYSIC_DAMAGE_ABSORPTION_AMOUNT,
			{ amp: Int, old: Float -> ((amp + 1) * LibConfig.SERVER.shieldAdditionalValuePerLevel.get()).toFloat() },
			null
		)
		register(
			LibMobEffects.SPIRIT_ABSORPTION_SHIELD,
			LibDamageTypes.SPIRIT.location(),
			LibAttachmentTypes.SPIRIT_DAMAGE_ABSORPTION_AMOUNT,
			{ amp: Int, old: Float -> ((amp + 1) * LibConfig.SERVER.shieldAdditionalValuePerLevel.get()).toFloat() },
			null
		)
		register(
			LibMobEffects.EROSION_ABSORPTION_SHIELD,
			LibDamageTypes.EROSION.location(),
			LibAttachmentTypes.EROSION_DAMAGE_ABSORPTION_AMOUNT,
			{ amp: Int, old: Float -> ((amp + 1) * LibConfig.SERVER.shieldAdditionalValuePerLevel.get()).toFloat() },
			null
		)
		register(
			LibMobEffects.SOUL_ABSORPTION_SHIELD,
			LibDamageTypes.THE_SOUL.location(),
			LibAttachmentTypes.SOUL_DAMAGE_ABSORPTION_AMOUNT,
			{ amp: Int, old: Float -> ((amp + 1) * LibConfig.SERVER.shieldAdditionalValuePerLevel.get()).toFloat() },
			null
		)
	}

	private fun register(
		effect: Holder<MobEffect>,
		damageTypeTag: ResourceLocation,
		attachment: Supplier<AttachmentType<Float>>,
		initialAmount: BiFunction<Int, Float, Float>,
		shieldBreakSound: SoundEvent?,
		shieldConflict: Boolean = true
	) {
		shields.add(ShieldEntry(effect, damageTypeTag, attachment, initialAmount, shieldBreakSound, shieldConflict))
	}

	@JvmRecord
	data class ShieldEntry(
		val effect: Holder<MobEffect>,
		val damageTypeTag: ResourceLocation,
		val attachment: Supplier<AttachmentType<Float>>,
		val initialAmount: BiFunction<Int, Float, Float>,
		val shieldBreakSound: SoundEvent?,
		val isShieldConflict: Boolean
	) {
		fun playShieldBreakSound(player: Player) {
			if (shieldBreakSound != null) {
				player.playSound(shieldBreakSound)
			}
		}
	}
}
