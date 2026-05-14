package architecture.goldenboughs_lib.util

import com.mojang.brigadier.context.CommandContext
import net.minecraft.commands.CommandSourceStack
import net.minecraft.commands.arguments.EntityArgument
import net.minecraft.server.level.ServerPlayer

object CommandUtil {
	@Suppress("CheckedExceptionsKotlin")
	@JvmStatic
	fun getTargetPlayer(context: CommandContext<CommandSourceStack>): ServerPlayer =
		EntityArgument.getPlayer(context, "target")
}