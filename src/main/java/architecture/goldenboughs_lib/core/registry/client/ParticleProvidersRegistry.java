package architecture.goldenboughs_lib.core.registry.client;

import architecture.goldenboughs_lib.client.particle.DyeingMagicCircleParticle;
import architecture.goldenboughs_lib.client.particle.LcDamageIconParticle;
import architecture.goldenboughs_lib.client.particle.magicbullet.MagicBulletMagicCircleParticle;
import architecture.goldenboughs_lib.client.particle.solemnlament.ButterflyParticle;
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
		event.registerSpriteSet(LibParticleTypes.DYEING_MAGIC_CIRCLE.get(), DyeingMagicCircleParticle.Provider::new);
		event.registerSpriteSet(LibParticleTypes.MAGIC_BULLET_MAGIC_CIRCLE.get(), MagicBulletMagicCircleParticle.Provider::new);
		event.registerSpriteSet(LibParticleTypes.SOLEMN_LAMENT_BUTTERFLY_BLACK.get(), ButterflyParticle.Provider::new);
		event.registerSpriteSet(LibParticleTypes.SOLEMN_LAMENT_BUTTERFLY_WHITE.get(), ButterflyParticle.Provider::new);
	}
}
