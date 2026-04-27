package architecture.goldenboughs_lib.core.registry;

import architecture.goldenboughs_lib.common.command.RationalityCommands;
import architecture.goldenboughs_lib.core.GoldenBoughsLib;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

/**
 * 指令事件
 */
@EventBusSubscriber(modid = GoldenBoughsLib.ID)
public final class CommandRegistry {
	@SubscribeEvent
	public static void registry(RegisterCommandsEvent event) {
		CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
		RationalityCommands.processRationality(dispatcher);
	}
}
