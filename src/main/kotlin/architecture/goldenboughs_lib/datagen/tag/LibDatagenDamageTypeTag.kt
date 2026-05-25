package architecture.goldenboughs_lib.datagen.tag

import architecture.goldenboughs_lib.core.Lib
import architecture.goldenboughs_lib.init.LibDamageTypes
import architecture.goldenboughs_lib.init.tag.LibDamageTypeTags
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.DamageTypeTagsProvider
import net.minecraft.tags.DamageTypeTags
import net.minecraft.world.damagesource.DamageTypes
import net.neoforged.neoforge.common.Tags
import net.neoforged.neoforge.common.data.ExistingFileHelper
import org.jetbrains.annotations.Contract
import java.util.concurrent.CompletableFuture
import architecture.goldenboughs_lib.core.LibConstants

class LibDatagenDamageTypeTag(
	output: PackOutput,
	lookupProvider: CompletableFuture<HolderLookup.Provider>,
	existingFileHelper: ExistingFileHelper
) : DamageTypeTagsProvider(output, lookupProvider, LibConstants.ID, existingFileHelper) {
	override fun addTags(provider: HolderLookup.Provider) {
		// 物理伤害
		tag(LibDamageTypeTags.PHYSICS).add(
			DamageTypes.FALLING_ANVIL,
			DamageTypes.FALLING_BLOCK,
			DamageTypes.FALLING_STALACTITE,
			DamageTypes.FIREWORKS,
			DamageTypes.MOB_ATTACK,
			DamageTypes.MOB_ATTACK_NO_AGGRO,
			DamageTypes.PLAYER_ATTACK,
			DamageTypes.SPIT,
			DamageTypes.STING,
			DamageTypes.SWEET_BERRY_BUSH,
			DamageTypes.THORNS,
			DamageTypes.THROWN,
			DamageTypes.TRIDENT,
			DamageTypes.UNATTRIBUTED_FIREBALL,
			DamageTypes.WIND_CHARGE,
			DamageTypes.ARROW,
			DamageTypes.CACTUS,
			DamageTypes.BAD_RESPAWN_POINT,
			DamageTypes.FALL,
			DamageTypes.FIREBALL,
			DamageTypes.FLY_INTO_WALL
		).addOptional(LibDamageTypes.PHYSICS.location())
		// 精神伤害
		tag(LibDamageTypeTags.SPIRIT).add(
			DamageTypes.MOB_PROJECTILE
		).addOptional(LibDamageTypes.SPIRIT.location())
		// 侵蚀伤害
		tag(LibDamageTypeTags.EROSION).add(
			DamageTypes.WITHER_SKULL,
			DamageTypes.WITHER
		).addOptional(LibDamageTypes.EROSION.location())
		// 灵魂伤害
		tag(LibDamageTypeTags.THE_SOUL).add(
			DamageTypes.SONIC_BOOM
		).addOptional(LibDamageTypes.THE_SOUL.location())
		// 绕过
		tag(LibDamageTypeTags.BYPASS_LC).add(
			DamageTypes.IN_WALL,
			DamageTypes.GENERIC,
			DamageTypes.FREEZE,
			DamageTypes.DRAGON_BREATH,
			DamageTypes.MAGIC,
			DamageTypes.INDIRECT_MAGIC,
			DamageTypes.FELL_OUT_OF_WORLD,
			DamageTypes.OUTSIDE_BORDER,
			DamageTypes.STARVE,
			DamageTypes.CRAMMING,
			DamageTypes.GENERIC_KILL
		).addTag(Tags.DamageTypes.IS_MAGIC)

		tag(DamageTypeTags.ALWAYS_HURTS_ENDER_DRAGONS)
			.addOptional(LibDamageTypes.PHYSICS.location())
			.addOptional(LibDamageTypes.SPIRIT.location())
			.addOptional(LibDamageTypes.EROSION.location())
			.addOptional(LibDamageTypes.THE_SOUL.location())
			.addOptional(LibDamageTypes.ABNORMALITIES.location())
			.addOptional(LibDamageTypes.EGO.location())
			.addOptional(LibDamageTypes.MELEE.location())
			.addOptional(LibDamageTypes.REMOTE.location())

		tag(DamageTypeTags.IS_PROJECTILE)
			.addOptional(LibDamageTypes.REMOTE.location())

		tag(DamageTypeTags.NO_ANGER)
			.addOptional(LibDamageTypes.REMOTE.location())
	}

	@Contract(pure = true)
	override fun getName(): String {
		return LibConstants.NAME + " Damage Type Tags"
	}
}
