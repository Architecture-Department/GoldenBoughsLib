package architecture.goldenboughs_lib.client.gui.hudlayers.chop_flavor

import architecture.goldenboughs_lib.api.AllOpe
import architecture.goldenboughs_lib.client.gui.hudlayers.BasicHudLayer
import architecture.goldenboughs_lib.event.ChopFlavorLayerEvent
import net.minecraft.client.DeltaTracker
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.world.item.ItemStack
import net.neoforged.fml.ModLoader
import java.util.function.Function


// TODO 等新系统
@AllOpe
class ChopFlavorLayer : BasicHudLayer() {
	private var chopFlavorBarProvider: Function<ItemStack, IChopFlavorBar>? = null
	private var activateBar: IChopFlavorBar? = null
	private var mainHandItemStack: ItemStack = ItemStack.EMPTY

	// 获取渲染进度
	//	public static float getRenderProgress(SkillContainer container, float partialTick) {
	//		boolean creative = container.getExecutor().getOriginal().isCreative();
	//		boolean fullstack = creative || container.isFull();
	//		return fullstack || container.isActivated() ? 1.0F : container.getResource(partialTick);
	//	}

	override fun init(guiGraphics: GuiGraphics, deltaTracker: DeltaTracker) {
		super.init(guiGraphics, deltaTracker)
		val mainHandItem = player?.mainHandItem ?: ItemStack.EMPTY
		if (mainHandItem != mainHandItemStack) {
			activateBar = chopFlavorBarProvider!!.apply(mainHandItem)
			mainHandItemStack = mainHandItem
		}
	}

	override fun sizeChange(newScreenWidth: Int, newScreenHeight: Int) {
		super.sizeChange(newScreenWidth, newScreenHeight)
		x = newScreenWidth / 2
		y = newScreenHeight - newScreenHeight / 4
	}

	override fun renderDrawLayer(guiGraphics: GuiGraphics, deltaTracker: DeltaTracker) {
		if (activateBar != null) {
			activateBar!!.render(guiGraphics, deltaTracker, leftPos, topPos)
		}
	}

	fun castSkill() {
		if (activateBar != null) {
			activateBar!!.castSkill()
		}
	}

	@AllOpe
	interface IChopFlavorBar {
		fun render(guiGraphics: GuiGraphics, deltaTracker: DeltaTracker, x: Int, y: Int)

		fun castSkill()
	}

	@AllOpe
	abstract class ChopFlavorBar : IChopFlavorBar {
		abstract override fun render(guiGraphics: GuiGraphics, deltaTracker: DeltaTracker, x: Int, y: Int)
	}

	companion object {
		@JvmField
		val INSTANCE: ChopFlavorLayer = ChopFlavorLayer()

		//	private LocalPlayerPatch localPlayerPatch;
		@JvmStatic
		fun init() {
			val event = ChopFlavorLayerEvent()
			event.clear()
			INSTANCE.chopFlavorBarProvider =
				ModLoader.postEventWithReturn(event).chopFlavorBarProvider
		}
	}
}
