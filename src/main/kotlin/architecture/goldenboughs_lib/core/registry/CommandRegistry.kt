package architecture.goldenboughs_lib.core.registry

import architecture.goldenboughs_lib.common.command.RationalityDataCommands
import architecture.goldenboughs_lib.core.LibConstants
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.event.RegisterCommandsEvent

/**
 * 指令事件
 */
@EventBusSubscriber(modid = LibConstants.ID)
object CommandRegistry {
	@SubscribeEvent
	fun registry(event: RegisterCommandsEvent) {
		val dispatcher = event.dispatcher
		RationalityDataCommands.registry(dispatcher)
	}
}
