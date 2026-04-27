package architecture.goldenboughs_lib.client.gui.hudlayers.shield;

import architecture.goldenboughs_lib.client.gui.hudlayers.StatusBarLayer;
import architecture.goldenboughs_lib.client.gui.widget.HorizontalStatusBar;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public abstract class ShieldBarLayer extends StatusBarLayer {

	protected final ResourceLocation TEXTURE;
	protected final ResourceLocation BOTTOM_TEXTURE;
	protected final ResourceLocation LIGHT_TEXTURE;
	protected final Holder<MobEffect> absorptionEffect;

	public ShieldBarLayer(ResourceLocation texture,
	                      ResourceLocation bottomTexture,
	                      ResourceLocation lightTexture,
	                      Holder<MobEffect> absorptionEffect) {
		super(new HorizontalStatusBar(93, 11,
			new HorizontalStatusBar.TextureLayer(0, 0, 93, 11, bottomTexture),
			new HorizontalStatusBar.TextureLayer(14, 2, 78, 7, texture),
			new HorizontalStatusBar.TextureLayer(0, 0, 93, 11, lightTexture))
		);
		TEXTURE = texture;
		BOTTOM_TEXTURE = bottomTexture;
		LIGHT_TEXTURE = lightTexture;
		this.absorptionEffect = absorptionEffect;
	}

	@Override
	protected float getMaxValueFromSource() {
		final MobEffectInstance effect = this.player.getEffect(absorptionEffect);
		final float level = architecture.goldenboughs_lib.config.LibConfig.SERVER.shieldAdditionalValuePerLevel.get().floatValue();
		if (Objects.isNull(effect)) {
			return 0;
		}

		return level * (effect.getAmplifier() + 1);
	}

	@Override
	protected void updateStatusBarAppearance() {

	}

	@Override
	public void renderStatusBar(GuiGraphics guiGraphics, @NotNull DeltaTracker deltaTracker) {
		if (getCurrentValueFromSource() != 0)
			super.renderStatusBar(guiGraphics, deltaTracker);
	}

	@Override
	protected float getCurrentValueFromSource() {
		float shieldAmount = 0.0f;
		for (var entry : architecture.goldenboughs_lib.init.LibAbsorptionShieldsRegistry.getAll()) {
			if (entry.effect().equals(absorptionEffect))
				shieldAmount = player.getData(entry.attachment().get());
		}
		return shieldAmount;
	}
}
