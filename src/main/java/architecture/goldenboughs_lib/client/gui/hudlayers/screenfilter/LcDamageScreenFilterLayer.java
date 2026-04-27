package architecture.goldenboughs_lib.client.gui.hudlayers.screenfilter;

import architecture.goldenboughs_lib.api.LcDamageType;
import architecture.goldenboughs_lib.client.gui.hudlayers.BasicHudLayer;
import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class LcDamageScreenFilterLayer extends BasicHudLayer {
	public static final ResourceLocation PHYSICS = GoldenBoughsLib.modRl("textures/gui/filter/physics.png");
	public static final ResourceLocation SPIRIT = GoldenBoughsLib.modRl("textures/gui/filter/spirit.png");
	public static final ResourceLocation EROSION = GoldenBoughsLib.modRl("textures/gui/filter/erosion.png");
	public static final ResourceLocation THE_SOUL = GoldenBoughsLib.modRl("textures/gui/filter/the_soul.png");
	public static final LcDamageScreenFilterLayer INSTANCE = new LcDamageScreenFilterLayer();

	private final Set<Filter> rationalityFilters = new LinkedHashSet<>(4);

	private final Map<LcDamageType, Filter> filterObjects = Map.of(
		LcDamageType.PHYSICS, new Filter(PHYSICS),
		LcDamageType.SPIRIT, new Filter(SPIRIT),
		LcDamageType.EROSION, new Filter(EROSION),
		LcDamageType.THE_SOUL, new Filter(THE_SOUL)
	);

	public LcDamageScreenFilterLayer() {
	}

	public void addFilter(@Nullable LcDamageType damageType) {
		Filter filter = this.filterObjects.getOrDefault(damageType != null ? damageType : PHYSICS, this.filterObjects.get(LcDamageType.PHYSICS));
		filter.setValue(1);
		this.rationalityFilters.add(filter);
	}

	@Override
	public int getWidth() {
		return getScreenWidth();
	}

	@Override
	public int getHeight() {
		return getScreenHeight();
	}

	@Override
	protected void renderDrawLayer(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
		if (this.rationalityFilters.isEmpty()) {
			return;
		}
		PoseStack pose = guiGraphics.pose();
		pose.pushPose();
		RenderSystem.disableDepthTest();
		RenderSystem.depthMask(false);
		RenderSystem.enableBlend();

		Iterator<Filter> iterator = this.rationalityFilters.iterator();
		float partialTick = Math.clamp(deltaTracker.getRealtimeDeltaTicks(), 0, 1);
		while (iterator.hasNext()) {
			Filter filter = iterator.next();
			if (filter.getAlpha() <= 0 && filter.currentValue <= 0 && filter.previousValue <= 0) {
				iterator.remove();
				continue;
			}
			filter.render(guiGraphics, 0, 0, partialTick);
		}

		RenderSystem.disableBlend();
		RenderSystem.depthMask(true);
		RenderSystem.enableDepthTest();
		guiGraphics.setColor(1, 1, 1, 1);
		pose.popPose();
	}

	public static class Filter extends RationalityScreenFilterLayer.Filter {
		protected float currentValue;
		protected float previousValue;

		public Filter(ResourceLocation texture) {
			super(texture);
		}

		public void reset() {
			super.setAlpha(1);
		}

		public void setValue(float value) {
			this.currentValue = value;
			this.previousValue = value;
		}

		@Override
		protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
			double clamp = Math.clamp(partialTick * 0.5, 0, 1);
			float calculatedRenderedValue = (float) Math.clamp(Mth.lerp(clamp, this.previousValue, this.currentValue), 0, 1);
			setAlpha(calculatedRenderedValue);
			super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
			if (this.previousValue != calculatedRenderedValue) {
				this.previousValue = calculatedRenderedValue;
			}
			this.currentValue -= (float) clamp;
		}
	}
}
