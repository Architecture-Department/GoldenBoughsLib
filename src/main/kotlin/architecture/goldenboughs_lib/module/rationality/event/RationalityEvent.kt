package architecture.goldenboughs_lib.module.rationality.event

import architecture.goldenboughs_lib.api.AllOpe
import architecture.goldenboughs_lib.init.LibAttributes
import net.minecraft.world.entity.ai.attributes.AttributeInstance
import net.minecraft.world.entity.player.Player
import net.neoforged.bus.api.ICancellableEvent
import net.neoforged.fml.event.IModBusEvent
import net.neoforged.neoforge.event.entity.player.PlayerEvent

@AllOpe
abstract class RationalityEvent(player: Player) : PlayerEvent(player), IModBusEvent {
	val max: AttributeInstance?
		get() = entity.getAttribute(LibAttributes.MAX_RATIONALITY)

	val naturalRecoveryRate: AttributeInstance?
		/**
		 * 获取自然恢复效率
		 */
		get() = entity.getAttribute(LibAttributes.RATIONALITY_NATURAL_RECOVERY_WAIT_TIME)

	val recoveryAmount: AttributeInstance?
		/**
		 * 获取自然恢复量
		 */
		get() = entity.getAttribute(LibAttributes.RATIONALITY_RECOVERY_AMOUNT)

	abstract class Modify(player: Player, val oldValue: Float) : RationalityEvent(player) {
		/**
		 * 可取消
		 */
		class Pre(player: Player, oldValue: Float, var newValue: Float) :
			Modify(player, oldValue), ICancellableEvent

		/**
		 * 不可取消
		 */
		class Post(player: Player, oldValue: Float, val newValue: Float) :
			Modify(player, oldValue)
	}
}
