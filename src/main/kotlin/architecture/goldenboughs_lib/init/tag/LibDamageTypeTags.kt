package architecture.goldenboughs_lib.init.tag

import architecture.goldenboughs_lib.core.Lib.modRl
import architecture.goldenboughs_lib.util.LibUtil.rlOf
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.TagKey
import net.minecraft.world.damagesource.DamageType

object LibDamageTypeTags {
	/**
	 * 物理
	 */
	@JvmField
	val PHYSICS: TagKey<DamageType> = createTag("physics")

	/**
	 * 精神
	 */
	@JvmField
	val SPIRIT: TagKey<DamageType> = createTag("spirit")

	/**
	 * 侵蚀
	 */
	@JvmField
	val EROSION: TagKey<DamageType> = createTag("erosion")

	/**
	 * 灵魂
	 */
	@JvmField
	val THE_SOUL: TagKey<DamageType> = createTag("the_soul")

	/**
	 * 绕过脑叶伤害
	 */
	@JvmField
	val BYPASS_LC: TagKey<DamageType> = createTag("bypass_lobotomy_corporation_damage")

	private fun createTag(name: String): TagKey<DamageType> {
		return createTag(modRl(name))
	}

	private fun createTag(location: ResourceLocation): TagKey<DamageType> {
		return TagKey.create(Registries.DAMAGE_TYPE, location)
	}

	private fun createCTag(name: String): TagKey<DamageType> {
		return createTag(rlOf("c", name))
	}

	private fun createMcTag(name: String): TagKey<DamageType> {
		return createTag(ResourceLocation.withDefaultNamespace(name))
	}
}
