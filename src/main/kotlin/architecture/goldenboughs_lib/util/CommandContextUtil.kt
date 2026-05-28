package architecture.goldenboughs_lib.util

import architecture.goldenboughs_lib.core.LibConstants
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.context.ParsedArgument

object CommandContextUtil {
	fun <T> CommandContext<T>.getArguments(): Map<String, ParsedArgument<T, *>>? {
		try {
			val argumentsField = javaClass.getDeclaredField("arguments")
			argumentsField.isAccessible = true
			return argumentsField.get(this) as? Map<String, ParsedArgument<T, *>>
		} catch (exception: Exception) {
			LibConstants.LOGGER.error("CommandContext.getArguments() error: ${exception.message}")
		}
		return null
	}
}
