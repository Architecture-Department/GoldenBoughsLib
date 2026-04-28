package architecture.goldenboughs_lib.client.gui.hudlayers.chop_flavor;


import architecture.goldenboughs_lib.client.gui.hudlayers.BasicHudLayer;
import architecture.goldenboughs_lib.event.ChopFlavorLayerEvent;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModLoader;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

// TODO 等新系统
public class ChopFlavorLayer extends BasicHudLayer {
	public static final ChopFlavorLayer INSTANCE = new ChopFlavorLayer();

	private Function<ItemStack, IChopFlavorBar> chopFlavorBarProvider;
	@Nullable
	private IChopFlavorBar activateBar;
	private ItemStack mainHandItemStack = ItemStack.EMPTY;
//	private LocalPlayerPatch localPlayerPatch;

	public static void init() {
		ChopFlavorLayerEvent event = new ChopFlavorLayerEvent();
		event.clear();
		INSTANCE.chopFlavorBarProvider = ModLoader.postEventWithReturn(event).getChopFlavorBarProvider();
	}

	// 获取渲染进度
//	public static float getRenderProgress(SkillContainer container, float partialTick) {
//		boolean creative = container.getExecutor().getOriginal().isCreative();
//		boolean fullstack = creative || container.isFull();
//		return fullstack || container.isActivated() ? 1.0F : container.getResource(partialTick);
//	}

	@Override
	public void init(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
		super.init(guiGraphics, deltaTracker);
		ItemStack mainHandItem = getPlayer().getMainHandItem();
		if (mainHandItem != mainHandItemStack) {
			activateBar = chopFlavorBarProvider.apply(mainHandItem);
			mainHandItemStack = mainHandItem;
		}
	}

	@Override
	protected void sizeChange(int newScreenWidth, int newScreenHeight) {
		super.sizeChange(newScreenWidth, newScreenHeight);
		setX(newScreenWidth / 2);
		setY(newScreenHeight - newScreenHeight / 4);
	}

	@Override
	public void playerChange(LocalPlayer newPlayer) {
		super.playerChange(newPlayer);
//		localPlayerPatch = EpicFightCapabilities.getEntityPatch(newPlayer, LocalPlayerPatch.class);
	}

	@Override
	protected void renderDrawLayer(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
		if (activateBar != null) {
			activateBar.render(guiGraphics, deltaTracker, leftPos, topPos);
		}
	}

	public final void castSkill() {
		if (activateBar != null) {
			activateBar.castSkill();
		}
	}

	public interface IChopFlavorBar {
		void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker, int x, int y);

		default void castSkill() {
		}
	}

	public static abstract class ChopFlavorBar implements IChopFlavorBar {
		@Override
		public abstract void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker, int x, int y);
	}
}
