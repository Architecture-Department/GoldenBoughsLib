package architecture.goldenboughs_lib.mixin.world.entity;

import architecture.goldenboughs_lib.event.PlayerHotbarChangeEvent;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.NeoForge;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 拦截玩家热键栏槽位切换（滚轮），触发 [PlayerHotbarChangeEvent]。
 * 滚轮切换通过 [Inventory.swapPaint] 实现。
 * 数字键 1-9 切换直接设置 [Inventory.selected] 字段，需额外处理。
 */
@Mixin(Inventory.class)
public abstract class InventoryMixin {
	@Shadow
	@Final
	public Player player;

	@Inject(at = @At("HEAD"), method = "swapPaint", cancellable = true)
	private void goldenboughs_lib$onSwapPaint(double direction, CallbackInfo ci) {
		Inventory self = (Inventory) (Object) this;
		int fromSlot = self.selected;
		// swapPaint 根据 direction 方向计算目标槽位
		int i = (int) Math.signum(direction);
		int toSlot = fromSlot - i;
		while (toSlot < 0) toSlot += 9;
		while (toSlot >= 9) toSlot -= 9;
		if (fromSlot == toSlot) return;

		ItemStack fromStack = self.getItem(fromSlot);
		ItemStack toStack = self.getItem(toSlot);

		PlayerHotbarChangeEvent event = new PlayerHotbarChangeEvent(player, fromSlot, toSlot, fromStack, toStack);
		NeoForge.EVENT_BUS.post(event);

		if (event.isCanceled()) {
			ci.cancel();
		}
	}
}
