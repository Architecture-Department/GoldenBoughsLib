package architecture.goldenboughs_lib.core.registry.client

import architecture.goldenboughs_lib.client.particle.LcDamageIconParticle
import architecture.goldenboughs_lib.client.particle.text.DamageTextParticle
import architecture.goldenboughs_lib.client.particle.text.TextParticleProvider
import architecture.goldenboughs_lib.core.LibConstants
import architecture.goldenboughs_lib.init.LibParticleTypes
import net.minecraft.client.particle.ParticleEngine
import net.minecraft.client.particle.SpriteSet
import net.neoforged.api.distmarker.Dist
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent

@EventBusSubscriber(modid = LibConstants.ID, value = [Dist.CLIENT])
object ParticleProvidersRegistry {
	@SubscribeEvent
	fun registry(event: RegisterParticleProvidersEvent) {
		event.registerSpecial(LibParticleTypes.TEXT.get(), TextParticleProvider())
		event.registerSpecial(
			LibParticleTypes.DAMAGE_TEXT.get(),
			DamageTextParticle.Provider()
		)
		event.registerSpriteSet(
			LibParticleTypes.LC_DAMAGE_ICON.get(),
			ParticleEngine.SpriteParticleRegistration { spriteSet: SpriteSet -> LcDamageIconParticle.Provider(spriteSet) })
	}
}
