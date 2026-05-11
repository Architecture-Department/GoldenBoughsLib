package architecture.goldenboughs_lib.common.item

import architecture.goldenboughs_lib.init.LibDataComponentTypes
import architecture.goldenboughs_lib.util.RationalityUtil.getRationalityValue
import architecture.goldenboughs_lib.util.RationalityUtil.modifyRationalityValue
import net.minecraft.client.Minecraft
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionHand
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

/**
 * 理智值控制工具
 */
class CreativeRationalityToolItem(properties: Properties) :
	Item(properties.component(LibDataComponentTypes.MODE_BOOLEAN, false)) {
	override fun use(
		world: Level,
		playerEntity: Player,
		handUsed: InteractionHand
	): InteractionResultHolder<ItemStack> {
		val itemStackInHand = playerEntity.getItemInHand(handUsed)
		if (!playerEntity.isCreative) {
			return InteractionResultHolder.fail(itemStackInHand)
		}

		if (playerEntity.isShiftKeyDown) {
			itemStackInHand.set(
				LibDataComponentTypes.MODE_BOOLEAN,
				false == itemStackInHand.get(LibDataComponentTypes.MODE_BOOLEAN)
			)
			return InteractionResultHolder.success(itemStackInHand)
		}

		if (world.isClientSide()) {
			return InteractionResultHolder.fail(itemStackInHand)
		}

		val value = when {
			false == itemStackInHand.get(LibDataComponentTypes.MODE_BOOLEAN) -> -1.0f
			else -> 1.0f
		}
		playerEntity.modifyRationalityValue(
			value, false
		)
		return InteractionResultHolder.success(itemStackInHand)
	}

	override fun inventoryTick(
		itemStack: ItemStack,
		world: Level,
		entity: Entity,
		slotIndex: Int,
		isCurrentlySelected: Boolean
	) {
		super.inventoryTick(itemStack, world, entity, slotIndex, isCurrentlySelected)
		if (entity is Player && !world.isClientSide() && isCurrentlySelected) {
			// TODO本地化
			displayRationalityValue("当前的理智值为：" + entity.getRationalityValue())
		}
	}

	private fun displayRationalityValue(message: String) {
		Minecraft.getInstance().gui.setOverlayMessage(Component.literal(message), false)
	}
}
