package architecture.goldenboughs_lib.linkage.jade

import architecture.goldenboughs_lib.core.Lib
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.level.block.Block
import snownee.jade.api.IWailaClientRegistration
import snownee.jade.api.IWailaCommonRegistration
import snownee.jade.api.IWailaPlugin
import snownee.jade.api.WailaPlugin
import architecture.goldenboughs_lib.core.LibConstants

@WailaPlugin
class ModJadePlugin : IWailaPlugin {
	override fun register(registration: IWailaCommonRegistration?) {
	}

	override fun registerClient(registration: IWailaClientRegistration) {
		registration.registerEntityComponent(EntityLcLevel.INSTANCE, Entity::class.java)
		registration.registerBlockComponent(BlockLcLevel.INSTANCE, Block::class.java)
		registration.registerEntityComponent(LivingEntityVulnerable.INSTANCE, LivingEntity::class.java)
	}

	companion object {
		@JvmField
		val ENTITY_LC_LEVEL: ResourceLocation = LibConstants.modRl("entity_lobotomy_corporation_level")

		@JvmField
		val BLOCK_LC_LEVEL: ResourceLocation = LibConstants.modRl("block_lobotomy_corporation_level")

		@JvmField
		val ENTITY_LC_VULNERABLE: ResourceLocation = LibConstants.modRl("entity_lobotomy_corporation_vulnerable")
	}
}
