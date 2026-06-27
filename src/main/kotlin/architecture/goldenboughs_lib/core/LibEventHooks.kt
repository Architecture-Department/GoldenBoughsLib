package architecture.goldenboughs_lib.core

import architecture.goldenboughs_lib.module.rationality.event.RationalityEvent
import net.minecraft.world.entity.player.Player
import net.neoforged.neoforge.common.NeoForge
import java.util.Map

object LibEventHooks {
	@JvmStatic
	fun sourceRationalityPre(player: Player, oldValue: Float, newValue: Float): MutableMap.MutableEntry<Boolean, Float> {
		val event = RationalityEvent.Modify.Pre(player, oldValue, newValue)
		NeoForge.EVENT_BUS.post(event)
		val canceled = event.isCanceled()
		return Map.entry(canceled, if (canceled) event.oldValue else event.newValue)
	}

	@JvmStatic
	fun sourceRationalityPost(player: Player, oldValue: Float, newValue: Float) {
		NeoForge.EVENT_BUS.post(RationalityEvent.Modify.Post(player, oldValue, newValue))
	}
}
