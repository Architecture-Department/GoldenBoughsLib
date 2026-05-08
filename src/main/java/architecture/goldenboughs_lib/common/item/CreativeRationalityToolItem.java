package architecture.goldenboughs_lib.common.item;

import architecture.goldenboughs_lib.init.LibDataComponentTypes;
import architecture.goldenboughs_lib.util.RationalityUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

/**
 * 理智值控制工具
 */
public class CreativeRationalityToolItem extends Item {
	public CreativeRationalityToolItem(Properties properties) {
		super(properties.component(LibDataComponentTypes.MODE_BOOLEAN, false));
	}

	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level world, Player playerEntity, @NotNull InteractionHand handUsed) {
		ItemStack itemStackInHand = playerEntity.getItemInHand(handUsed);
		if (!playerEntity.isCreative()) {
			return InteractionResultHolder.fail(itemStackInHand);
		}

		if (playerEntity.isShiftKeyDown()) {
			itemStackInHand.set(LibDataComponentTypes.MODE_BOOLEAN, Boolean.FALSE.equals(itemStackInHand.get(LibDataComponentTypes.MODE_BOOLEAN)));
			return InteractionResultHolder.success(itemStackInHand);
		}

		if (world.isClientSide()) {
			return InteractionResultHolder.fail(itemStackInHand);
		}

		RationalityUtil.modifyValue(playerEntity, Boolean.TRUE == itemStackInHand.get(LibDataComponentTypes.MODE_BOOLEAN) ? -1 : 1, false);
		return InteractionResultHolder.success(itemStackInHand);
	}

	@Override
	public void inventoryTick(@NotNull ItemStack itemStack, @NotNull Level world, @NotNull Entity entity, int slotIndex, boolean isCurrentlySelected) {
		super.inventoryTick(itemStack, world, entity, slotIndex, isCurrentlySelected);
		if (entity instanceof Player player && !world.isClientSide() && isCurrentlySelected) {
			displayRationalityValue("当前的理智值为：" + RationalityUtil.getValue(player));
		}
	}

	private void displayRationalityValue(String message) {
		Minecraft.getInstance().gui.setOverlayMessage(Component.literal(message), false);
	}
}
