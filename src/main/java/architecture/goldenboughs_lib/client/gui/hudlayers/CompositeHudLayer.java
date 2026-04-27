package architecture.goldenboughs_lib.client.gui.hudlayers;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BooleanSupplier;

/**
 * 组合层
 */
public abstract class CompositeHudLayer extends IHudLayer {
	private final Map<IHudLayer, BooleanSupplier> layers = new LinkedHashMap<>();
	protected int screenWidth;
	protected int screenHeight;

	public CompositeHudLayer() {
		this.minecraft = Minecraft.getInstance();
		this.font = this.minecraft.font;
	}

	public CompositeHudLayer addLayer(IHudLayer layer) {
		this.layers.put(layer, () -> true);
		return this;
	}

	public CompositeHudLayer addLayer(IHudLayer layer, BooleanSupplier supplier) {
		this.layers.put(layer, supplier);
		return this;
	}

	public Set<Map.Entry<IHudLayer, BooleanSupplier>> getLayers() {
		return this.layers.entrySet();
	}

	@Override
	public void render(final GuiGraphics guiGraphics, final DeltaTracker deltaTracker) {
		if (this.minecraft.options.hideGui) {
			return;
		}

		init(guiGraphics, deltaTracker);
		renderSubLayer(guiGraphics, deltaTracker);
	}

	public void init(final GuiGraphics guiGraphics, final DeltaTracker deltaTracker) {
		int newScreenWidth = guiGraphics.guiWidth();
		int newScreenHeight = guiGraphics.guiHeight();

		boolean isWidthChange = newScreenWidth != this.screenWidth;
		boolean isHeightChange = newScreenHeight != this.screenHeight;
		if (isWidthChange || isHeightChange) {
			sizeChange(isWidthChange, isHeightChange, newScreenWidth, newScreenHeight);
		}

		if (this.font != this.minecraft.font) {
			this.font = this.minecraft.font;
			updateSubLayerFont(this.font);
		}

		var newPlayer = Objects.requireNonNull(this.minecraft.player);
		if (this.player != newPlayer) {
			playerChange(newPlayer);
		}

		subInit(guiGraphics, deltaTracker);
	}

	protected void subInit(final GuiGraphics guiGraphics, final DeltaTracker deltaTracker) {
		this.layers.keySet().forEach((layer) -> layer.init(guiGraphics, deltaTracker));
	}

	protected void updateSubLayerFont(final Font font) {
		this.layers.forEach((layer, supplier) -> layer.setFont(font));
	}

	public void playerChange(final LocalPlayer newPlayer) {
		this.player = newPlayer;
		updateSubLayerPlayer(newPlayer);
	}

	protected void updateSubLayerPlayer(final LocalPlayer newPlayer) {
		this.layers.keySet().forEach((layer) -> layer.playerChange(newPlayer));
	}

	protected final void sizeChange(final boolean isWidthChange, final boolean isHeightChange, final int newScreenWidth, final int newScreenHeight) {
		if (isWidthChange) {
			this.screenWidth = newScreenWidth;
		}

		if (isHeightChange) {
			this.screenHeight = newScreenHeight;
		}

		updatePos(isWidthChange, isHeightChange, newScreenWidth, newScreenHeight);
		this.layers.forEach((layer, supplier) -> {
			if (isHeightChange) {
				layer.setTopPos(this.topPos);
			}

			if (isWidthChange) {
				layer.setLeftPos(this.leftPos);
			}
		});
	}

	protected void updatePos(final boolean isWidthChange, final boolean isHeightChange, final int newScreenWidth, final int newScreenHeight) {
	}

	protected void renderSubLayer(final GuiGraphics guiGraphics, final DeltaTracker deltaTracker) {
		PoseStack pose = guiGraphics.pose();
		pose.pushPose();
		for (Map.Entry<IHudLayer, BooleanSupplier> entry : this.layers.entrySet()) {
			IHudLayer layer = entry.getKey();
			BooleanSupplier supplier = entry.getValue();
			if (!supplier.getAsBoolean()) {
				continue;
			}
			pose.translate(0, -layer.getHeight(), 0);
			layer.render(guiGraphics, deltaTracker);
			pose.translate(0, -2, 0);
		}
		pose.popPose();
	}

	public int getWidth() {
		return this.layers.entrySet().stream()
			.filter(a -> a.getValue().getAsBoolean())
			.mapToInt(entry -> entry.getKey().getWidth())
			.max()
			.orElse(0);
	}

	public int getHeight() {
		if (this.layers.isEmpty()) {
			return 0;
		}

		AtomicInteger atomicInteger = new AtomicInteger(0);
		int height = this.layers.entrySet().stream()
			.filter(a -> a.getValue().getAsBoolean())
			.mapToInt(entry -> {
				atomicInteger.incrementAndGet();
				return entry.getKey().getHeight();
			}).sum();
		int count = atomicInteger.get();
		if (count == 0) {
			return 0;
		}
		return count - 1 + height;
	}
}
