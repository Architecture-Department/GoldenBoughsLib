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

/**
 * 拦截玩家热键栏槽位切换（滚轮），触发 [PlayerHotbarChangeEvent]。
 * <p>
 * [swapPaint] 仅改变 [selected] 字段，无其他副作用，
 * 取消时直接恢复 [fromSlot]，重定向时直接设置目标槽位。
 * <p>
 * 数字键 1-9 切换由 [MinecraftKeybindsMixin] 的 PUTFIELD 包裹处理。
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
		original.call(direction);
		int toSlot = selected;

		PlayerHotbarChangeEvent event = NeoForge.EVENT_BUS.post(
			new PlayerHotbarChangeEvent(player, fromSlot, toSlot,
				getItem(fromSlot), getItem(toSlot))
		);

		if (event.isCanceled()) {
			selected = fromSlot;
			return;
		}

		selected = event.getToSlot();
	}
}
