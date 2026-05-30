package architecture.goldenboughs_lib.core

import architecture.goldenboughs_lib.module.rationality.event.RationalityEvent.Modify
import net.minecraft.world.entity.player.Player
import net.neoforged.fml.ModLoader
import java.util.Map

object LibEventHooks {
	@JvmStatic
	fun sourceRationalityPre(
		player: Player, oldValue: Float, newValue: Float
	): MutableMap.MutableEntry<Boolean, Float> {
		val event = Modify.Pre(player, oldValue, newValue)
		ModLoader.postEvent<Modify.Pre>(event)
		val canceled = event.isCanceled()
		return Map.entry<Boolean, Float>(
			canceled, if (canceled) event.oldValue else event.newValue
		)
	}

	@JvmStatic
	fun sourceRationalityPost(player: Player, oldValue: Float, newValue: Float) {
		ModLoader.postEvent(Modify.Post(player, oldValue, newValue))
	}
}
