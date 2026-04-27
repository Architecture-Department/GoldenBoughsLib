package architecture.goldenboughs_lib.linkage.jade;

import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public final class ModJadePlugin implements IWailaPlugin {
	public static final ResourceLocation ENTITY_LC_LEVEL = GoldenBoughsLib.modRl("entity_lobotomy_corporation_level");
	public static final ResourceLocation BLOCK_LC_LEVEL = GoldenBoughsLib.modRl("block_lobotomy_corporation_level");
	public static final ResourceLocation ENTITY_LC_VULNERABLE = GoldenBoughsLib.modRl("entity_lobotomy_corporation_vulnerable");

	@Override
	public void register(IWailaCommonRegistration registration) {
	}

	@Override
	public void registerClient(IWailaClientRegistration registration) {
		registration.registerEntityComponent(EntityLcLevel.INSTANCE, Entity.class);
		registration.registerBlockComponent(BlockLcLevel.INSTANCE, Block.class);
		registration.registerEntityComponent(LivingEntityVulnerable.INSTANCE, LivingEntity.class);
	}
}
