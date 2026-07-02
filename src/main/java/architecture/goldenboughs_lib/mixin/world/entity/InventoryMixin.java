package architecture.goldenboughs_lib.mixin.world.entity;

import architecture.goldenboughs_lib.event.PlayerHotbarChangeEvent;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
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

	@Shadow
	public int selected;

	@Shadow
	public abstract ItemStack getItem(int index);

	@WrapMethod(method = "swapPaint")
	private void goldenboughs_lib$onSwapPaint(
		double direction,
		Operation<Void> original
	) {
		int fromSlot = selected;

		// 使用源代码来切换获取槽位（以便适配有多个槽位的MOD修改）
		original.call(direction);
		int toSlot = selected;

		ItemStack fromStack = getItem(fromSlot);
		ItemStack toStack = getItem(toSlot);

		PlayerHotbarChangeEvent event = NeoForge.EVENT_BUS.post(new PlayerHotbarChangeEvent(player, fromSlot, toSlot, fromStack, toStack));

		if (event.isCanceled()){
			selected = fromSlot;
			return;
		}

		double direction1 = Math.floorMod(event.getToSlot() - event.getFromSlot(), 9);
		original.call(direction1);
	}
}
