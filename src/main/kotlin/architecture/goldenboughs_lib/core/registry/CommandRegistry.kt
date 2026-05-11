package architecture.goldenboughs_lib.core.registry

import architecture.goldenboughs_lib.common.command.RationalityCommands
import architecture.goldenboughs_lib.core.Lib
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.RegisterCommandsEvent

/**
 * 指令事件
 */
@EventBusSubscriber(modid = Lib.ID)
object CommandRegistry {
	@SubscribeEvent
	fun registry(event: RegisterCommandsEvent) {
		val dispatcher = event.dispatcher
		RationalityCommands.processRationality(dispatcher)
	}
}
