package architecture.goldenboughs_lib.core.registry

import architecture.goldenboughs_lib.common.command.RationalityCommands
import architecture.goldenboughs_lib.core.GoldenBoughsLib
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.RegisterCommandsEvent

/**
 * 指令事件
 */
@EventBusSubscriber(modid = GoldenBoughsLib.ID)
object CommandRegistry {
	@SubscribeEvent
	@JvmStatic
	fun registry(event: RegisterCommandsEvent) {
		val dispatcher = event.dispatcher
		RationalityCommands.processRationality(dispatcher)
	}
}
