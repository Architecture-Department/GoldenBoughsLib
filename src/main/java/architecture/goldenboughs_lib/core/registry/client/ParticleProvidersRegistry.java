package architecture.goldenboughs_lib.core.registry.client;

import architecture.goldenboughs_lib.client.particle.LcDamageIconParticle;
import architecture.goldenboughs_lib.client.particle.text.DamageTextParticle;
import architecture.goldenboughs_lib.client.particle.text.TextParticleProvider;
import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import architecture.goldenboughs_lib.init.LibParticleTypes;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(modid = GoldenBoughsLib.ID, value = Dist.CLIENT)
public final class ParticleProvidersRegistry {
	@SubscribeEvent
	public static void registry(RegisterParticleProvidersEvent event) {
		event.registerSpecial(LibParticleTypes.TEXT.get(), new TextParticleProvider());
		event.registerSpecial(LibParticleTypes.DAMAGE_TEXT.get(), new DamageTextParticle.Provider());
		event.registerSpriteSet(LibParticleTypes.LC_DAMAGE_ICON.get(), LcDamageIconParticle.Provider::new);
	}
}
